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

package co.rsk.net.handler.txvalidator;

import org.ethereum.core.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigInteger;

public class TxValidatorGasLimitValidatorTest {
    @Test
    public void validGasLimit() {
        Transaction tx1 = Mockito.mock(Transaction.class);
        Transaction tx2 = Mockito.mock(Transaction.class);

        Mockito.when(tx1.getGasLimit()).thenReturn(BigInteger.valueOf(1).toByteArray());
        Mockito.when(tx2.getGasLimit()).thenReturn(BigInteger.valueOf(6).toByteArray());

        byte[] gl = BigInteger.valueOf(6).toByteArray();

        TxValidatorGasLimitValidator tvglv = new TxValidatorGasLimitValidator();

        Assert.assertTrue(tvglv.validate(tx1, null, gl, null, Long.MAX_VALUE));
        Assert.assertTrue(tvglv.validate(tx2, null, gl, null, Long.MAX_VALUE));
    }

    @Test
    public void invalidGasLimit() {

        Transaction tx1 = Mockito.mock(Transaction.class);

        Mockito.when(tx1.getGasLimit()).thenReturn(BigInteger.valueOf(6).toByteArray());

        byte[] gl = BigInteger.valueOf(3).toByteArray();

        TxValidatorGasLimitValidator tvglv = new TxValidatorGasLimitValidator();

        Assert.assertFalse(tvglv.validate(tx1, null, gl, null, Long.MAX_VALUE));
    }
}
