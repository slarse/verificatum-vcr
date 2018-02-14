
/*
 * Copyright 2008-2018 Douglas Wikstrom
 *
 * This file is part of Verificatum Core Routines (VCR).
 *
 * VCR is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * VCR is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with VCR. If not, see <http://www.gnu.org/licenses/>.
 */

package com.verificatum.arithm;

import com.verificatum.crypto.RandomSource;
import com.verificatum.eio.ByteTreeBasic;
import com.verificatum.eio.ByteTreeConvertible;
import com.verificatum.util.Functions;
import com.verificatum.eio.ByteTreeReader;


/**
 * Represents an immutable permutation which allows the permutation
 * and its inverse to be applied to arrays of elements.
 *
 * @author Douglas Wikstrom
 */
public abstract class Permutation implements ByteTreeConvertible {

    /**
     * Token upper bound for indexes of a permutation.
     */
    static final LargeInteger UPPER_BOUND = LargeInteger.ONE.shiftLeft(64);

    /**
     * Permutation as an array of integers.
     */
    LargeIntegerArray table;

    /**
     * Creates an empty instance that must be populated by subclasses.
     */
    protected Permutation() {
        // For subclasses.
    }

    /**
     * Generates an instance represented by the input.
     *
     * @param table Representation of a permutation.
     */
    protected Permutation(final LargeIntegerArray table) {
        this.table = table;
    }

    /**
     * Generates the identity permutation of a given size.
     *
     * @param numberOfElements Number of elements to permute.
     */
    public Permutation(final int numberOfElements) {
        table = LargeIntegerArray.consecutive(0, numberOfElements);
    }

    /**
     * Creates a permutation from the input representation.
     *
     * @param size Expected size of the permutation.
     * @param btr Representation of permutation.
     * @throws ArithmFormatException If the input is incorrect or has
     *  wrong size.
     */
    public Permutation(final int size, final ByteTreeReader btr)
        throws ArithmFormatException {
        this.table =
            LargeIntegerArray.toLargeIntegerArray(size,
                                                  btr,
                                                  LargeInteger.ZERO,
                                                  new LargeInteger(size));
    }

    /**
     * Generates the identity permutation of a given size.
     *
     * @param numberOfElements Number of elements to permute.
     * @return Identity permutation of a given size.
     */
    public static Permutation identity(final int numberOfElements) {
        if (LargeIntegerArray.inMemory) {
            return new PermutationIM(numberOfElements);
        } else {
            return new PermutationF(numberOfElements);
        }
    }

    /**
     * Creates a permutation from the input representation.
     *
     * @param size Expected size of the permutation.
     * @param btr Representation of permutation.
     * @return Permutation represented by the input.
     * @throws ArithmFormatException If the input is incorrect or has
     *  wrong size.
     */
    public static Permutation toPermutation(final int size,
                                            final ByteTreeReader btr)
        throws ArithmFormatException {
        if (LargeIntegerArray.inMemory) {
            return new PermutationIM(size, btr);
        } else {
            return new PermutationF(size, btr);
        }
    }

    /**
     * Creates a permutation from the input representation.
     *
     * @param size Expected size of permutation.
     * @param btr Representation of permutation.
     * @return Permutation represented by the input.
     */
    public static Permutation unsafeToPermutation(final int size,
                                                  final ByteTreeReader btr) {
        try {
            return toPermutation(size, btr);
        } catch (final ArithmFormatException afe) {
            throw new ArithmError("Unable to read permutation!", afe);
        }
    }

    /**
     * Generates a random permutation of a given size using the given
     * source of randomness.
     *
     * @param numberOfElements Number of elements to permute.
     * @param statDist Decides the statistical distance from the
     * uniform distribution.
     * @param randomSource Source of randomness used to generate the
     * permutation.
     * @return Random permutation of the given size.
     */
    public static Permutation random(final int numberOfElements,
                                     final RandomSource randomSource,
                                     final int statDist) {
        if (LargeIntegerArray.inMemory) {
            return new PermutationIM(numberOfElements, randomSource, statDist);
        } else {
            return new PermutationF(numberOfElements, randomSource, statDist);
        }
    }

    /**
     * Returns the number of permuted elements.
     *
     * @return Number of permuted elements.
     */
    public int size() {
        return table.size();
    }

    /**
     * Frees the resources allocated by this instance.
     */
    public void free() {
        table.free();
    }

    /**
     * Maps an index with the permutation.
     *
     * @param index Index to be mapped.
     * @return Image of input index.
     */
    public int map(final int index) {
        return table.get(index).intValue();
    }

    /**
     * Returns the inverse of this permutation.
     *
     * @return Inverse of this permutation.
     */
    public abstract Permutation inv();

    /**
     * Returns a permutation representing this permutation but shrunk
     * to the given size.
     *
     * @param size Size of new permutation.
     * @return A shrunken permutation.
     */
    public abstract Permutation shrink(final int size);

    @Override
    public ByteTreeBasic toByteTree() {

        // Make sure that we use fixed size representation of all
        // indices.
        final LargeInteger liSize = new LargeInteger(size());
        final int expectedByteLength = liSize.toByteArray().length;

        return table.toByteTree(expectedByteLength);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        final LargeIntegerIterator lii = this.table.getIterator();

        while (lii.hasNext()) {
            sb.append(", ");
            sb.append(lii.next().intValue());
        }
        lii.close();

        return "[" + sb.substring(1) + " ]";
    }

    @Override
    public int hashCode() {
        return Functions.hashCode(this);
    }
}
