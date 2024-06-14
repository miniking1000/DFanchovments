package org.pythonchik.dfanchovments;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The various type of CEnchantments that may be added to armour or weapons
 */
public abstract class CEnchantment extends Enchantment {

    private final NamespacedKey key;

    public CEnchantment(@NotNull NamespacedKey key) {
        super();
        this.key = key;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Gets the unique name of this enchantment
     *
     * @return Unique name
     * @deprecated CEnchantments are badly named, use {@link #getKey()}.
     */
    @NotNull
    public abstract String getName();

    /**
     * Gets the maximum level that this CEnchantment may become.
     *
     * @return Maximum level of the CEnchantment
     */
    public abstract int getMaxLevel();

    /**
     * Gets the level that this CEnchantment should start at
     *
     * @return Starting level of the CEnchantment
     */
    public abstract int getStartLevel();

    /**
     * Gets the type of {@link ItemStack} that may fit this CEnchantment.
     *
     * @return Target type of the CEnchantment
     */
    @NotNull
    public abstract EnchantmentTarget getItemTarget();

    /**
     * Checks if this enchantment is a treasure enchantment.
     * <br>
     * Treasure CEnchantments can only be received via looting, trading, or
     * fishing.
     *
     * @return true if the enchantment is a treasure enchantment
     */
    public abstract boolean isTreasure();

    /**
     * Checks if this enchantment is a cursed enchantment
     * <br>
     * Cursed CEnchantments are found the same way treasure CEnchantments are
     *
     * @return true if the enchantment is cursed
     * @deprecated cursed CEnchantments are no longer special. Will return true
     * only for {@link CEnchantment#BINDING_CURSE} and
     * {@link CEnchantment#VANISHING_CURSE}.
     */

    public abstract boolean isCursed();

    /**
     * Check if this enchantment conflicts with another enchantment.
     *
     * @param other The enchantment to check against
     * @return True if there is a conflict.
     */
    public abstract boolean conflictsWith(@NotNull CEnchantment other);

    /**
     * Checks if this CEnchantment may be applied to the given {@link
     * ItemStack}.
     * <p>
     * This does not check if it conflicts with any CEnchantments already
     * applied to the item.
     *
     * @param item Item to test
     * @return True if the enchantment may be applied, otherwise False
     */
    public abstract boolean canEnchantItem(@NotNull ItemStack item);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CEnchantment)) {
            return false;
        }
        final CEnchantment other = (CEnchantment) obj;
        if (!this.key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "CEnchantment[" + key + ", " + getName() + "]";
    }


    /**
     * Checks if this is accepting CEnchantment registrations.
     *
     * @return True if the server Implementation may add CEnchantments
     */


    public List<String> getTragers(){
        return new ArrayList<>();
    }

}
