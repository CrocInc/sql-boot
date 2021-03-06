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

package com.github.mgramin.sqlboot.model.resourcetypelist

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.SimpleEndpoint
import com.github.mgramin.sqlboot.model.dialect.FakeDialect
import com.github.mgramin.sqlboot.model.resourcetypelist.impl.FsResourceTypeList
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 5231745cc5b9b08cedd762394593203cd13cbd25 $
 * @since 0.1
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = ["/test_config.xml"])
class FsResourceTypeListTest {

    private val dbMd = SimpleEndpoint()
    private val connections = listOf(dbMd)

    init {
        dbMd.name = "unit_test_db_md"
        dbMd.host = "127.0.0.1"
        dbMd.confDir = "conf/h2/sql/database"
        dbMd.properties = mapOf(
                "sql_dialect" to "h2",
                "jdbc_url" to "jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';",
                "jdbc_driver_class_name" to "org.h2.Driver"
        )
    }

    @Test
    fun resourceTypes() {
        assertEquals(sequenceOf("locks", "query", "sessions", "column", "index", "constraint", "table", "schema").sorted().toList(),
                FsResourceTypeList(connections, emptyList()).types().map { it.name() }.sorted())
    }

}