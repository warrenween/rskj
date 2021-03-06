/*
 * This file is part of RskJ
 * Copyright (C) 2017 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package co.rsk.mine;

import org.ethereum.config.Constants;
import org.ethereum.config.SystemProperties;

import java.math.BigInteger;

/**
 * Created by Ruben on 23/05/2016.
 * This class calculates next block gas limit
 */
public class GasLimitCalculator {

    public GasLimitCalculator() {}

    // At the end of this algorithm it will increase the gas limit if and only if the previous block gas used
    // is above 2/3 of the block gas limit, and decrease otherwise, by small amounts
    // The idea is to increase the gas limit when there are more transactions on the network while reduce it when
    // there are no or almost no transaction on it
    public BigInteger calculateBlockGasLimit(BigInteger parentGasLimit, BigInteger parentGasUsed, BigInteger minGasLimit, BigInteger targetGasLimit) {
        Constants constants = SystemProperties.CONFIG.getBlockchainConfig().getCommonConstants();

        BigInteger newGasLimit = parentGasLimit;

        // decay = parentGasLimit / 1024
        // current Eth implementation substracts parentGasLimit / 1024 - 1
        BigInteger decay = parentGasLimit.divide(BigInteger.valueOf(constants.getGAS_LIMIT_BOUND_DIVISOR()));

        // contrib = (parentGasUsed * 3 / 2) / 1024
        BigInteger contrib = parentGasUsed.multiply(BigInteger.valueOf(3));
        contrib = contrib.divide(BigInteger.valueOf(2));
        contrib = contrib.divide(BigInteger.valueOf(constants.getGAS_LIMIT_BOUND_DIVISOR()));

        newGasLimit = newGasLimit.subtract(decay);
        newGasLimit = newGasLimit.add(contrib);

        // Gas limit can never be lesser than a certain threshold
       if (newGasLimit.compareTo(minGasLimit) < 0)
           newGasLimit = minGasLimit;

        if (newGasLimit.compareTo(targetGasLimit) > 0)
            newGasLimit = targetGasLimit;

        return newGasLimit;
    }
}
