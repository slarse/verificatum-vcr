
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

package com.verificatum.eio;

/**
 * Thrown when a fatal error occurs. This should never be caught.
 *
 * @author Douglas Wikstrom
 */
public class EIOError extends Error {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new error with the specified detail message.
     *
     * @param message Detailed message of the problem.
     */
    public EIOError(final String message) {
        super(message);
    }

    /**
     * Constructs a new error with the specified detail message and
     * cause.
     *
     * @param message Detailed message of the problem.
     * @param cause What caused this error to be thrown.
     */
    public EIOError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
