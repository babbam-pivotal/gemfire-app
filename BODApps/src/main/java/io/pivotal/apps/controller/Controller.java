/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.apps.controller;

import java.io.IOException;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.apps.service.CATService;
import io.pivotal.apps.service.IVRService;
import io.pivotal.events.CxpEventQueryParams;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/corp", produces = {APPLICATION_JSON_VALUE})
public class Controller {

	
    private final IVRService ivrService;
    private final CATService catService;
    
    @Autowired
    public Controller(IVRService ivrService, CATService catService) {

    	this.ivrService = ivrService;
        this.catService = catService;
    }
    
    @RequestMapping( value = { "/events" }, method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<?> getADSLShapedInfo(
    		@RequestParam( value = "eventTypeId", required = true) Long eventTypeId, 
            @RequestParam( value = "customerId", required = true) String customerId,
            @RequestParam( value = "customerIdTypeId", required = true ) Long customerIdTypeId,
            @RequestParam( value = "queryMode", required = false, defaultValue = "historical" )  CxpEventQueryParams.QueryMode queryMode,
            @RequestParam( value = "earliest", required = false ) @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME ) OffsetDateTime earliest,
            @RequestParam( value = "latest", required = false ) @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME ) OffsetDateTime latest ) throws IOException 
    {
        return ivrService.get(eventTypeId, customerId, customerIdTypeId, queryMode, earliest, latest);
    }

    @RequestMapping(value = "/events/{CUST_ID}", method = RequestMethod.GET)
    @ResponseBody
    public String getCATInfo(@PathVariable("CUST_ID") String CUST_ID) throws IOException {
        return catService.get(CUST_ID);
    }
    
   
}
