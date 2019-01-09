/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.rest.controllers

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.sql.select.impl.JdbcSelectQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors.toList
import javax.sql.DataSource

/**
 * Created by mgramin on 31.12.2016.
 */
@RestController
@EnableAutoConfiguration
@CrossOrigin
class SqlController(@field:Autowired private val dataSource: DataSource) {

    @RequestMapping(value = ["sql"], produces = arrayOf(MediaType.APPLICATION_XML_VALUE))
    @Throws(BootException::class)
    fun execSql2Xml(@RequestBody sql: String): List<Map<String, Any>> {
        return JdbcSelectQuery(dataSource, sql).select().collect(toList())
    }

    @RequestMapping(value = ["exec"], method = arrayOf(RequestMethod.POST), produces = arrayOf(MediaType.APPLICATION_XML_VALUE))
    @Throws(BootException::class)
    fun execSql2XmlPost(@RequestBody sql: String): List<Map<String, Any>> {
        return JdbcSelectQuery(dataSource, sql).select().collect(toList())
    }

    @RequestMapping(value = ["exec"], produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Throws(BootException::class)
    fun execSql2Json(@RequestParam("sql") sql: String): List<Map<String, Any>> {
        return JdbcSelectQuery(dataSource, sql).select().collect(toList())
    }

    @RequestMapping(value = ["exec"], method = arrayOf(RequestMethod.POST), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Throws(BootException::class)
    fun execSql2JsonPost(@RequestBody sql: String): List<Map<String, Any>> {
        return JdbcSelectQuery(dataSource, sql).select().collect(toList())
    }

}