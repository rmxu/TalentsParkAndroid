/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.net.ftp;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Custom {@link TrustManager} implementation.
 * 
 * @version $Id: FTPSTrustManager.java,v 1.1 2009/01/24 04:37:02 julianweigle Exp $
 * @since 2.0
 */
public class FTPSTrustManager implements X509TrustManager
{
    /**
     * No-op
     */
    public void checkClientTrusted(X509Certificate[] certificates, String authType)
    {
        return;
    }

    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException
    {
        for (int i = 0; i < certificates.length; ++i)
        {
            certificates[i].checkValidity();
        }
    }

    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }
}
