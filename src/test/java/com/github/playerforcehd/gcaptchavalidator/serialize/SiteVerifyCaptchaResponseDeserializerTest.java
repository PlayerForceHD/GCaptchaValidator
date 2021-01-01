/*
 * The MIT License
 *
 * Copyright (c) 2021 Pascal Zarrad
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

package com.github.playerforcehd.gcaptchavalidator.serialize;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidationResponse;
import com.github.playerforcehd.gcaptchavalidator.data.ClientType;
import com.github.playerforcehd.gcaptchavalidator.data.ValidationError;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for the default site verify response deserializer.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class SiteVerifyCaptchaResponseDeserializerTest {
    @Test(dataProvider = "deserializeWithVersion2DataProvider")
    public void testDeserializeWithVersion2(String testResponse) {
        SiteVerifyCaptchaResponseDeserializer deserializer = new SiteVerifyCaptchaResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        assertTrue(result.hasSucceeded());
        assertEquals(result.getScore(), -1f);
        assertEquals(result.getAction(), "");
        assertEquals(result.getChallengeTimestamp().getTime(), 1609262462000L);
        assertEquals(result.getClientType(), ClientType.WEB);
        assertEquals(result.getHostnameOrPackageName(), "localhost");
        assertEquals(result.getErrors().length, 0);
    }

    @DataProvider
    public Object[][] deserializeWithVersion2DataProvider() {
        return new Object[][] {
            {
                "{\"success\": true,\"challenge_ts\": \"2020-12-29T17:21:02Z\",\"hostname\": \"localhost\"}"
            }
        };
    }

    @Test(dataProvider = "deserializeWithVersion2DataWithoutSuccessProvider")
    public void testDeserializeWithVersion2WithoutSuccess(String testResponse) {
        SiteVerifyCaptchaResponseDeserializer deserializer = new SiteVerifyCaptchaResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        assertFalse(result.hasSucceeded());
        assertEquals(result.getScore(), -1f);
        assertEquals(result.getAction(), "");
        assertEquals(result.getChallengeTimestamp().getTime(), 1609262462000L);
        assertEquals(result.getClientType(), ClientType.WEB);
        assertEquals(result.getHostnameOrPackageName(), "localhost");
        assertEquals(result.getErrors().length, 0);
    }

    @DataProvider
    public Object[][] deserializeWithVersion2DataWithoutSuccessProvider() {
        return new Object[][] {
            {
                "{\"success\": false,\"challenge_ts\": \"2020-12-29T17:21:02Z\",\"hostname\": \"localhost\"}"
            }
        };
    }

    @Test(dataProvider = "deserializeWithVersion3DataProvider")
    public void testDeserializeWithVersion3(String testResponse) {
        SiteVerifyCaptchaResponseDeserializer deserializer = new SiteVerifyCaptchaResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        assertTrue(result.hasSucceeded());
        assertEquals(result.getScore(), 0.5f);
        assertEquals(result.getAction(), "home");
        assertEquals(result.getChallengeTimestamp().getTime(), 1609262462000L);
        assertEquals(result.getClientType(), ClientType.WEB);
        assertEquals(result.getHostnameOrPackageName(), "localhost");
        assertEquals(result.getErrors().length, 0);
    }

    @DataProvider
    public Object[][] deserializeWithVersion3DataProvider() {
        return new Object[][] {
            {
                "{\"success\": true,\"score\": 0.5,\"action\": \"home\"," +
                    "\"challenge_ts\": \"2020-12-29T17:21:02Z\",\"hostname\": \"localhost\"}"
            }
        };
    }

    @Test(dataProvider = "deserializeWithPlatformAndroidDataProvider")
    public void testDeserializeWithPlatformAndroid(String testResponse) {
        SiteVerifyCaptchaResponseDeserializer deserializer = new SiteVerifyCaptchaResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        assertTrue(result.hasSucceeded());
        assertEquals(result.getScore(), -1f);
        assertEquals(result.getAction(), "");
        assertEquals(result.getChallengeTimestamp().getTime(), 1609262462000L);
        assertEquals(result.getClientType(), ClientType.ANDROID);
        assertEquals(result.getHostnameOrPackageName(), "com.github.playerforcehd.gcaptchavalidator");
        assertEquals(result.getErrors().length, 0);
    }

    @DataProvider
    public Object[][] deserializeWithPlatformAndroidDataProvider() {
        return new Object[][] {
            {
                "{\"success\": true,\"challenge_ts\": \"2020-12-29T17:21:02Z\"," +
                    "\"apk_package_name\": \"com.github.playerforcehd.gcaptchavalidator\"}"
            }
        };
    }

    @Test(dataProvider = "deserializeWithErrorCodesDataProvider")
    public void testDeserializeWithErrorCodes(String testResponse) {
        SiteVerifyCaptchaResponseDeserializer deserializer = new SiteVerifyCaptchaResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        assertFalse(result.hasSucceeded());
        assertEquals(result.getScore(), -1f);
        assertEquals(result.getAction(), "");
        assertNull(result.getChallengeTimestamp());
        assertNull(result.getClientType());
        assertEquals(result.getHostnameOrPackageName(), "");
        ValidationError[] expectedErrors = {
            ValidationError.MISSING_INPUT_SECRET,
            ValidationError.MISSING_INPUT_RESPONSE
        };
        assertEquals(result.getErrors(), expectedErrors);
    }

    @DataProvider
    public Object[][] deserializeWithErrorCodesDataProvider() {
        return new Object[][] {
            {
                "{\"success\": false, \"error-codes\": [\"missing-input-secret\", \"missing-input-response\"]}"
            }
        };
    }
}
