/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Christian Schudt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package rocks.xmpp.extensions.vcard.avatar;

import org.testng.Assert;
import org.testng.annotations.Test;
import rocks.xmpp.core.XmlTest;
import rocks.xmpp.extensions.vcard.avatar.model.AvatarUpdate;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

/**
 * @author Christian Schudt
 */
public class AvatarTest extends XmlTest {
    protected AvatarTest() throws JAXBException, XMLStreamException {
        super(AvatarUpdate.class);
    }

    @Test
    public void marshalVCardUpdateWithEmptyPhotoElement() throws JAXBException, XMLStreamException {
        AvatarUpdate avatarUpdate = new AvatarUpdate("");
        String xml = marshal(avatarUpdate);
        Assert.assertEquals("<x xmlns=\"vcard-temp:x:update\"><photo></photo></x>", xml);
    }

    @Test
    public void marshalVCardUpdateWithNoPhotoElement() throws JAXBException, XMLStreamException {
        AvatarUpdate avatarUpdate = new AvatarUpdate();
        String xml = marshal(avatarUpdate);
        Assert.assertEquals("<x xmlns=\"vcard-temp:x:update\"></x>", xml);
    }
}