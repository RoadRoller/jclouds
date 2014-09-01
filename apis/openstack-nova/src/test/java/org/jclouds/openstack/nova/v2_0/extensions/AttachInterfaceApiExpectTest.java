/***************       BEGIN-STANDARD-COPYRIGHT      ***************

 Copyright (c) 2009-2014, Spirent Communications.

 All rights reserved. Proprietary and confidential information of Spirent Communications.

 ***************        END-STANDARD-COPYRIGHT       ***************/

package org.jclouds.openstack.nova.v2_0.extensions;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.FixedIP;
import org.jclouds.openstack.nova.v2_0.domain.InterfaceAttachment;
import org.jclouds.openstack.nova.v2_0.domain.PortState;
import org.jclouds.openstack.nova.v2_0.internal.BaseNovaApiExpectTest;
import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test(groups = "unit", testName = "AttachInterfaceApiExpectTest")
public class AttachInterfaceApiExpectTest extends BaseNovaApiExpectTest {

   public void testAttachInterfacesList() throws Exception {
      HttpRequest list = HttpRequest.builder().method("GET")
            .endpoint("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/servers/instance-1/os-interface")
            .addHeader("Accept", "application/json").addHeader("X-Auth-Token", authToken).build();

      HttpResponse listResponse = HttpResponse.builder().statusCode(200)
            .payload(payloadFromResource("/attach_interfaces_list.json")).build();

      NovaApi novaApi = requestsSendResponses(keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse, list, listResponse);

      FluentIterable<InterfaceAttachment> interfaceAttachments = novaApi.getAttachInterfaceApi("az-1.region-a.geo-1")
            .get().list("instance-1");

      Optional<? extends InterfaceAttachment> interfaceAttachment = interfaceAttachments.first();

      assertTrue(interfaceAttachment.isPresent(), "Couldn't find interface attachment");
      assertEquals(interfaceAttachment.get(), testInterfaceAttachment());
   }

   public void testAttachInterfaceGet() throws Exception {
      HttpRequest list = HttpRequest
            .builder()
            .method("GET")
            .endpoint(
                  "https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/servers/instance-1/os-interface/ce531f90-199f-48c0-816c-13e38010b442")
            .addHeader("Accept", "application/json").addHeader("X-Auth-Token", authToken).build();

      HttpResponse listResponse = HttpResponse.builder().statusCode(200)
            .payload(payloadFromResource("/attach_interface_details.json")).build();

      NovaApi novaApi = requestsSendResponses(keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse, list, listResponse);

      InterfaceAttachment interfaceAttachment = novaApi.getAttachInterfaceApi("az-1.region-a.geo-1").get()
            .get("instance-1", "ce531f90-199f-48c0-816c-13e38010b442");

      assertEquals(interfaceAttachment, testInterfaceAttachment());
   }

   private InterfaceAttachment testInterfaceAttachment() {
      return InterfaceAttachment
            .builder()
            .portId("ce531f90-199f-48c0-816c-13e38010b442")
            .networkId("3cb9bc59-5699-4588-a4b1-b87f96708bc6")
            .portState(PortState.ACTIVE)
            .macAddress("fa:16:3e:4c:2c:30")
            .fixedIps(
                  ImmutableSet.of(FixedIP.builder().ipAddress("192.168.1.3")
                        .subnetId("f8a6e8f8-c2ec-497c-9f23-da9616de54ef").build())).build();
   }

}
