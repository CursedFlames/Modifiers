package cursedflames.modifiers.common.reforge;

import java.util.Random;

import cursedflames.modifiers.common.ModifierHandler;
import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.config.Config;
import cursedflames.modifiers.common.modifier.curio.ModifierHandlerCurio;
import cursedflames.modifiers.common.util.XpUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerReforge extends Container {
	// TODO add slots for curios and armor?
	@ObjectHolder(ModifiersMod.MODID + ":reforge")
	public static ContainerType<ContainerReforge> CONTAINER_REFORGE;

	public BlockPos stationPos;
	private PlayerEntity player;
	private IItemHandler playerInventory;

	public ItemStackHandler stackHandler;
	private Slot stackSlot;

	public ContainerReforge(int windowId, World world, BlockPos pos, PlayerInventory playerInventory,
			PlayerEntity player) {
		super(CONTAINER_REFORGE, windowId);

		this.stationPos = pos;
		this.player = player;
		this.playerInventory = new InvWrapper(playerInventory);

		stackHandler = new ItemStackHandler() {
			@Override
			protected void onContentsChanged(int slot) {
				ItemStack stack = this.getStackInSlot(slot);				
				if (Config.REFORGE_CURIO_ENABLED.get()
						&& ModifierHandler.canHaveCurioModifier(stack)) {
					ModifierHandlerCurio.genCurioModifier(stack);
					CompoundNBT tag = stack.getOrCreateTag();
					if (!tag.contains("reforgeCost")) {
						tag.putInt("reforgeCost", getCurioReforgeCost(player.world.rand));
					}
				}
			}
		};
		addSlot(stackSlot = new SlotItemHandler(stackHandler, 0, 26, 44));
		layoutPlayerInventorySlots(8, 80);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(player.world, stationPos), player, Blocks.SMITHING_TABLE);
	}

	private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
		for (int i = 0; i < amount; i++) {
			addSlot(new SlotItemHandler(handler, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}

	private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount,
			int dy) {
		for (int j = 0; j < verAmount; j++) {
			index = addSlotRange(handler, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}

	private void layoutPlayerInventorySlots(int leftCol, int topRow) {
		// Player inventory
		addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

		// Hotbar
		topRow += 58;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
		
		// Offhand
		this.addSlot(new SlotItemHandler(playerInventory, 40, leftCol+9*18, topRow));
	}

	@Override
	public void onContainerClosed(PlayerEntity player) {
		super.onContainerClosed(player);
		ItemStack stack = stackHandler.getStackInSlot(0);
		// to make sure stack doesn't stay in container on world exit
		stackHandler.setStackInSlot(0, ItemStack.EMPTY);
		if (!player.isAlive()
				|| player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).hasDisconnected()) {
			player.dropItem(stack, false);
		} else {
			player.inventory.placeItemBackInInventory(player.world, stack);
		}
	}

	@Override // why does this not have a functional default implementation...
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		// TODO make this actually behave properly
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			itemstack = stack.copy();
			if (index == 0) {
				if (!this.mergeItemStack(stack, 1, 38, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stack, itemstack);
			} else {
				if (stackSlot.isItemValid(stack)) {
					if (!this.mergeItemStack(stack, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index < 28) {
					if (!this.mergeItemStack(stack, 28, 37, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index < 37 && !this.mergeItemStack(stack, 1, 28, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (stack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, stack);
		}

		return itemstack;
	}
	
	public static int getCurioReforgeCost(Random rand) {
		int min = Config.REFORGE_COST_CURIO_MIN.get();
		int max = Config.REFORGE_COST_CURIO_MAX.get();
		if (min>max) {
			int min_ = min;
			min = max;
			max = min_;
		}
		return rand.nextInt(max-min+1)+min;
	}
	
	public void tryDoReforge() {
		ItemStack stack = stackHandler.getStackInSlot(0);
		if (ModifierHandler.canHaveModifier(stack)) {
			boolean creative = player.isCreative();
			if (ModifierHandler.canHaveCurioModifier(stack)) {
				if (!Config.REFORGE_CURIO_ENABLED.get()) return;
				CompoundNBT tag = stack.getOrCreateTag();
				int xpCost;
				if (tag.contains("reforgeCost")) {
					xpCost = tag.getInt("reforgeCost");
				} else {
					ModifiersMod.logger.info("warning: item didn't have reforgeCost");
					return;
				}
				 // TODO do we still need this, and is it still accurate in 1.14
				int playerXp = XpUtil.getPlayerXP(player);
				if (creative || playerXp >= xpCost) {
					if (!creative) {
						XpUtil.addPlayerXP(player, -xpCost);
					}
					ModifierHandlerCurio.genCurioModifier(stack, true);
					tag.putInt("reforgeCost", getCurioReforgeCost(player.world.rand));
					float vol = (float) (Math.random()*0.3+0.9);
					float pitch = (float) (Math.random()*0.3+0.85);
					player.world.playSound(null, stationPos, SoundEvents.BLOCK_ANVIL_USE,
							SoundCategory.BLOCKS, vol, pitch);
				}
			} else {
				// tool modifier
			}
		}
	}
}
