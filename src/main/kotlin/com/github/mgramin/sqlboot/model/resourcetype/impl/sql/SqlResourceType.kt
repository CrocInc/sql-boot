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

package com.github.mgramin.sqlboot.model.resourcetype.impl.sql

import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resource.impl.DbResourceImpl
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.sql.select.SelectQuery
import org.apache.commons.lang3.StringUtils.strip

/**
 * Created by MGramin on 12.07.2017.
 */
class SqlResourceType(
        @field:Transient private val selectQuery: SelectQuery,
        private val aliases: List<String>
) : ResourceType {

    override fun aliases(): List<String> {
        return aliases
    }

    override fun path(): List<String> {
        return selectQuery.columns().keys
                .filter { v -> v.startsWith("@") }
                .map { v -> strip(v, "@") }
                .toList()
    }

    override fun read(uri: Uri): Sequence<DbResource> {
        return selectQuery.execute(hashMapOf("uri" to uri))
                .map { o ->
                    val path = o.entries
                            .filter { v -> v.key.startsWith("@") }
                            .map { it.value }
                            .toList()

                    val name = path[path.size - 1].toString()

                    val headers = o.entries
                            .map { strip(it.key, "@") to it.value }
                            .toMap()
                    DbResourceImpl(name, this,
                            DbUri(this.name(), path.map { it.toString() }.toList()),
                            headers)
                }
    }

    override fun metaData(): Map<String, String> {
        return selectQuery.columns()
    }
}