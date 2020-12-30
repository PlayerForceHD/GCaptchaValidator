package com.github.playerforcehd.gcaptchavalidator.request;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidatorConfiguration;
import com.github.playerforcehd.gcaptchavalidator.ValidatorConfiguration;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.testng.Assert.*;

/**
 * Test for the default captcha request handling implementation.
 *
 * Note that this test is an integration test instead of a unit test
 * as the interaction with the external service should work as a whole.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class ReCaptchaCaptchaRequestHandlerTest {

    /**
     * A test secret key provided by Google
     */
    private final String gReCaptchaTestSecret = "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe";

    /**
     * The remote ip used for testing
     */
    private final String remoteIP = "127.0.0.1";

    /**
     * The valid response used for testing
     */
    private final String acceptedResponse =
        "03AJz9lvRRl27ls2cnen32HC_LRxepB9xmLps0GcDMJfIGHIOaPWW29X-_DlvNGo5Tmx6lANsU" +
        "Hhko4CpKLYKvTLQQDjLrefVEyl7A5nuF26FsooF_GQ_O5r-EOX_FbAQ0RVc9vGrI7Lk_Bp_JzukTPdq4WgP-qSLbYErV-btJIwYh9MNm" +
        "xrFn-5RUqC08T4WnSp6-er8nAt2YwkqM1hKTlsMm-6VulyQD49UwoJ-Y_YBah8v4snxw-KI-8Fa09gQp0a449BK6N5XiH9AfUbH7V7f_" +
        "jRXuIUu22HTLJBz3AfHH-P5t6U9ZsY4";

    /**
     * The {@link WireMockServer} used to mock a WebServer to test the execution of WebHooks
     */
    private WireMockServer wireMockServer;

    /**
     * Initializes the WireMockServer used to test the requests
     */
    @BeforeMethod
    public void prepare() {
        // Start a mocked WebServer
        this.wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        this.wireMockServer.start();
    }

    /**
     * Stop the used {@link WireMockServer}
     */
    @AfterMethod
    public void reset() {
        // Stop a mocked WebServer
        this.wireMockServer.stop();
    }

    /**
     * Setup the stubs that handle the
     */
    private void setupWebStubs() {
        // Stub validation with everything set
        this.wireMockServer.stubFor(post(urlPathEqualTo("/recaptcha/api/siteverify"))
            .withHeader(
                "User-Agent",
                equalTo(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36"
                )
            )
            .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
            .withRequestBody(
                equalTo("secret=" +
                    this.gReCaptchaTestSecret + "&response=" + this.acceptedResponse +
                    "&remoteip=" + this.remoteIP
                )
            )
            .willReturn(
                aResponse()
                    .withBody(
                        "{ \"success\": true, \"challenge_ts\": \"2019-06-17T20:33:57Z\", " +
                            "\"hostname\": \"testkey.google.com\" }"
                    )
            )
        );
        // Stub validation without remote ip set
        this.wireMockServer.stubFor(post(urlPathEqualTo("/recaptcha/api/siteverify"))
            .withHeader(
                "User-Agent",
                equalTo(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36"
                )
            )
            .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
            .withRequestBody(
                equalTo(
                    "secret=" + this.gReCaptchaTestSecret +
                        "&response=" + this.acceptedResponse
                )
            )
            .willReturn(
                aResponse()
                    .withBody(
                        "{ \"success\": true, \"challenge_ts\": \"2019-06-17T20:33:57Z\", " +
                            "\"hostname\": \"testkey.google.com\" }"
                    )
            ));

        // Stub validation with wrong response and without remote ip set
        this.wireMockServer.stubFor(post(urlPathEqualTo("/recaptcha/api/siteverify"))
            .withHeader(
                "User-Agent",
                equalTo(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36"
                )
            )
            .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
            .withRequestBody(
                equalTo("secret=" + this.gReCaptchaTestSecret + "&response=THIS+IS+A+INVALID+RESPONSE")
            )
            .willReturn(
                aResponse()
                    .withBody(
                        "{ \"success\": false, " +
                            "\"error-codes\": [\"invalid-input-response\", \"invalid-input-secret\"] }"
                    )
            )
        );
        // Stub validation without any configuration
        this.wireMockServer.stubFor(post(urlPathEqualTo("/recaptcha/api/siteverify"))
            .withHeader(
                "User-Agent",
                equalTo(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36"
                )
            )
            .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
            .withRequestBody(equalTo("secret=&response="))
            .willReturn(
                aResponse()
                    .withBody(
                        "{ \"success\": false, " +
                            "\"error-codes\": [\"invalid-input-response\", \"invalid-input-secret\"] }"
                    )
            ));
    }

    /**
     * Returns the fake SiteVerify URL
     */
    private String getMockedSiteVerifyURL() {
        return this.wireMockServer.baseUrl() + "/recaptcha/api/siteverify";
    }

    /**
     * Tests the Google ReCaptcha Validation synchronous with a given RemoteIP
     */
    @Test
    public void testRequestWithRemoteIPSet() {
        final String expectedResult = "{ \"success\": true, \"challenge_ts\": \"2019-06-17T20:33:57Z\", " +
            "\"hostname\": \"testkey.google.com\" }";

        // Prepare Test on mock server
        this.setupWebStubs();

        // Test
        CaptchaValidatorConfiguration configuration = new ValidatorConfiguration(
            this.gReCaptchaTestSecret,
            this.getMockedSiteVerifyURL()
        );

        ReCaptchaCaptchaRequestHandler request = new ReCaptchaCaptchaRequestHandler();
        try {
            String result = request.request(configuration, this.acceptedResponse, this.remoteIP);
            assertEquals(result, expectedResult);
        } catch (CaptchaRequestHandlerException e) {
            fail("Failed to validate response", e);
        }
    }

    @Test
    public void testGCaptchaValidationWithoutRemoteIPSet() {
        final String expectedResult = "{ \"success\": true, \"challenge_ts\": \"2019-06-17T20:33:57Z\", " +
            "\"hostname\": \"testkey.google.com\" }";

        // Prepare Test on mock server
        this.setupWebStubs();

        // Test
        CaptchaValidatorConfiguration configuration = new ValidatorConfiguration(
            this.gReCaptchaTestSecret,
            this.getMockedSiteVerifyURL()
        );

        ReCaptchaCaptchaRequestHandler request = new ReCaptchaCaptchaRequestHandler();
        try {
            String result = request.request(configuration, this.acceptedResponse, "");
            assertEquals(result, expectedResult);
        } catch (CaptchaRequestHandlerException e) {
            fail("Failed to validate response", e);
        }
    }

    @Test
    public void testGCaptchaValidationWithoutRemoteIPSetAndWrongResponse() {
        final String expectedResult = "{ \"success\": false, " +
            "\"error-codes\": [\"invalid-input-response\", \"invalid-input-secret\"] }";

        // Prepare Test on mock server
        this.setupWebStubs();

        // Test
        CaptchaValidatorConfiguration configuration = new ValidatorConfiguration(
            this.gReCaptchaTestSecret,
            this.getMockedSiteVerifyURL()
        );

        ReCaptchaCaptchaRequestHandler request = new ReCaptchaCaptchaRequestHandler();
        try {
            String result = request.request(configuration, "THIS IS A INVALID RESPONSE", "");
            assertEquals(result, expectedResult);
        } catch (CaptchaRequestHandlerException e) {
            fail("Failed to validate response", e);
        }
    }

    @Test
    public void testGCaptchaValidationWithoutAnything() {
        final String expectedResult = "{ \"success\": false, " +
            "\"error-codes\": [\"invalid-input-response\", \"invalid-input-secret\"] }";

        // Prepare Test on mock server
        this.setupWebStubs();

        // Test
        CaptchaValidatorConfiguration configuration = new ValidatorConfiguration(
            "",
            this.getMockedSiteVerifyURL()
        );

        ReCaptchaCaptchaRequestHandler request = new ReCaptchaCaptchaRequestHandler();
        try {
            String result = request.request(configuration, "", "");
            assertEquals(result, expectedResult);
        } catch (CaptchaRequestHandlerException e) {
            fail("Failed to validate response", e);
        }
    }
}
