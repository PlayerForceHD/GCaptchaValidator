/*
 * The MIT License
 *
 * Copyright (c) 2016 Pascal Zarrad
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

package com.github.playerforcehd.gcaptchavalidator.captchaconfiguration;

import java.util.concurrent.ExecutorService;

/**
 * A builder for easy creation of the {@link CaptchaValidationConfiguration}
 *
 * @author PlayerForceHD
 * @version 1.0.0
 * @since 1.0.0
 */
public class CaptchaValidationConfigurationBuilder {

    /**
     * The configuration that is build with the current builder instance
     */
    private CaptchaValidationConfiguration captchaValidationConfiguration;

    public CaptchaValidationConfigurationBuilder() {
        this.captchaValidationConfiguration = new CaptchaValidationConfiguration();
    }

    /**
     * Creates a new {@link CaptchaValidationConfigurationBuilder} for easy creation of a {@link CaptchaValidationConfiguration}
     *
     * @return A new builder to make it easy to create a {@link CaptchaValidationConfiguration}
     */
    public static CaptchaValidationConfigurationBuilder builder() {
        return new CaptchaValidationConfigurationBuilder();
    }

    /**
     * Set the shared key between your site and ReCaptcha.
     *
     * @param secret The shared key between your site and ReCaptcha.
     * @return The instance of this builder
     */
    public CaptchaValidationConfigurationBuilder withSecret(String secret) {
        this.captchaValidationConfiguration.setSecret(secret);
        return this;
    }

    /**
     * Set the user response token provided by ReCaptcha, verifying the user on your site.
     *
     * @param response The user response token provided by ReCaptcha, verifying the user on your site.
     * @return The instance of this builder
     * @deprecated Since {@link com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator} v1.3.0, the response is an additional argument of the method to get off the one time usage of a request
     */
    @Deprecated
    public CaptchaValidationConfigurationBuilder withResponse(String response) {
        this.captchaValidationConfiguration.setResponse(response);
        return this;
    }

    /**
     * Optional: Set user's IP address.
     *
     * @param remoteIP The user's IP address.
     * @return The instance of this builder
     */
    public CaptchaValidationConfigurationBuilder withRemoteIP(String remoteIP) {
        this.captchaValidationConfiguration.setRemoteIP(remoteIP);
        return this;
    }

    /**
     * Optional: Set a custom {@link ExecutorService} to use instead of the default one.
     *
     * @param executorService A custom {@link ExecutorService} to use instead of the default one created by GCaptchaValidator
     * @return The instance of this builder
     */
    public CaptchaValidationConfigurationBuilder withExecutorService(ExecutorService executorService) {
        this.captchaValidationConfiguration.setExecutorService(executorService);
        return this;
    }

    /**
     * Returns the built configuration
     *
     * @return The configuration built with this builder
     */
    public CaptchaValidationConfiguration build() {
        return captchaValidationConfiguration;
    }

}
