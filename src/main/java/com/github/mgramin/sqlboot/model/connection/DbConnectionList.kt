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

package com.github.mgramin.sqlboot.model.connection

import com.github.mgramin.sqlboot.exceptions.BootException
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.lang.String.format
import java.util.*
import java.util.stream.Collectors

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 059927ff87fb2f47a67537fa336f6e838d08c1ea $
 * @since 0.1
 */
@Service
@Configuration
@ConfigurationProperties(prefix = "conf")
open class DbConnectionList {

    var connections: List<DbConnection> = ArrayList()

    val defaultConnection: DbConnection
        get() = connections.stream()
                .findFirst()
                .orElseThrow { BootException("Default connection not found.") }

    fun getConnectionByName(name: String?): DbConnection {
        return if (name == null) {
            defaultConnection
        } else connections.stream()
                .filter { v -> v.name.equals(name, ignoreCase = true) }
                .findFirst()
                .orElseThrow {
                    BootException(
                            format("Connection with name <<%s>> not found.", name))
                }
    }


    fun getConnectionsByMask(name: String?): List<DbConnection> {
        return if (name == null) {
            listOf(defaultConnection)
        } else connections.stream()
                .filter { v -> v.name!!.matches(name.toRegex()) }
                .collect(Collectors.toList())
    }

}