/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, CROC Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.mgramin.sqlboot.model.resourcetype.wrappers.http

import com.github.mgramin.sqlboot.model.connection.SimpleEndpointList
import com.github.mgramin.sqlboot.model.dialect.DbDialectList
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.ParallelWrapper
import com.github.mgramin.sqlboot.model.resourcetypelist.CacheWrapper
import com.github.mgramin.sqlboot.model.resourcetypelist.impl.FsResourceTypeList
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.wrappers.SqlPlaceholdersWrapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 69e9609bd238163b6b97346900c17bb6efd0c037 $
 * @since 0.1
 */
@RestController("DbResourceHttpJsonWrapper")
@ComponentScan(basePackages = ["com.github.mgramin.sqlboot"])
@CrossOrigin
class JsonWrapper {

    @Autowired
    private lateinit var endpointList: SimpleEndpointList

    @Autowired
    private lateinit var dbDialectList: DbDialectList

    private val logger = LoggerFactory.getLogger(this::class.java)

    @RequestMapping(value = ["/api/{connection}/{type}"], method = [GET, POST])
    fun read(@PathVariable connection: String,
             @PathVariable type: String,
             request: ServerHttpRequest): Flux<Map<String, Any>> {
        val uriString = if (request.queryParams.isNotEmpty()) {
            val query = request.queryParams.map { it.key + "=" + it.value.toString().replace("[", "").replace("]", "") }.joinToString(separator = "&")
            "$connection/$type?${query}"
        } else {
            "$connection/$type"
        }
        return getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri(uriString)))
    }

    @RequestMapping(value = ["/api/{connection}/{type}/{path}"], method = [GET, POST])
    fun read(@PathVariable connection: String,
             @PathVariable type: String,
             @PathVariable path: String,
             request: ServerHttpRequest): Flux<Map<String, Any>> {
        val uriString = if (request.queryParams.isNotEmpty()) {
            val query = request.queryParams.map { it.key + "=" + it.value.toString().replace("[", "").replace("]", "") }.joinToString(separator = "&")
            "$connection/$type/$path?${query}"
        } else {
            "$connection/$type"
        }
        return getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri(uriString)))
    }

    private fun getListResponseEntityHeaders(uri: Uri): Flux<Map<String, Any>> {
        val connections = endpointList.getByMask(uri.connection())
        return ParallelWrapper(
//                CacheWrapper
                (FsResourceTypeList(connections, dbDialectList.dialects)).types())
                .read(uri)
                .map { it.headers() }
    }

}
