/*
 * $Id: ChatStringToString.java 19250 2010-08-30 16:53:14Z dirk.olmes $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package global;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;

import fpdu.*;

/**
 * <code>NameStringToChatString</code> is a dummy transformer used in the hello world
 * application to transform the ChatString object into a string.
 */
public class FPDUToString extends AbstractTransformer
{
    public FPDUToString()
    {
        super();
        this.registerSourceType(DataTypeFactory.create(FPDU.class));
        this.setReturnDataType(DataTypeFactory.STRING);
    }

    @Override
    public Object doTransform(Object src, String encoding) throws TransformerException
    {
        return ((FPDU)src).toString();
    }
}
