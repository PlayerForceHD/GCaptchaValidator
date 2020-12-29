/*
 * The MIT License
 *
 * Copyright (c) 2020 Pascal Zarrad
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

package com.github.playerforcehd.gcaptchavalidator;

import com.github.playerforcehd.gcaptchavalidator.data.ClientType;
import com.github.playerforcehd.gcaptchavalidator.data.ValidationError;

import java.util.Date;

/**
 * An implementation of the {@link CaptchaValidationResponseVersion2}.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public final class ValidationResponseVersion2 implements CaptchaValidationResponseVersion2 {
    /**
     * The state if the response was successful
     */
    private final boolean succeeded;

    /**
     * The timestamp when the challenge has been loaded.
     */
    private final Date challengeTimestamp;

    /**
     * The type of client that send the response
     */
    private final ClientType clientType;

    /**
     * The hostname/ip or android package name
     */
    private final String hostnameOrPackageName;

    /**
     * The errors returned by the request (if there are any)
     */
    private final ValidationError[] errors;

    /**
     * Constructor
     *
     * @param succeeded The state if the validation was positive or negative
     * @param challengeTimestamp The timestamp when the challenge has been loaded
     * @param clientType The type of the client that send the validation request
     * @param hostnameOrPackageName The hostname/ip or android package name of the client
     * @param errors The errors returned in the response
     */
    ValidationResponseVersion2(
        boolean succeeded,
        Date challengeTimestamp,
        ClientType clientType,
        String hostnameOrPackageName,
        ValidationError[] errors
    ) {
        this.succeeded = succeeded;
        this.challengeTimestamp = challengeTimestamp;
        this.clientType = clientType;
        this.hostnameOrPackageName = hostnameOrPackageName;
        this.errors = errors;
    }

    @Override
    public boolean hasSucceeded() {
        return this.succeeded;
    }

    @Override
    public Date getChallengeTimestamp() {
        return this.challengeTimestamp;
    }

    @Override
    public ClientType getClientType() {
        return this.clientType;
    }

    @Override
    public String getHostnameOrPackageName() {
        return this.hostnameOrPackageName;
    }

    @Override
    public ValidationError[] getErrors() {
        return this.errors;
    }
}
