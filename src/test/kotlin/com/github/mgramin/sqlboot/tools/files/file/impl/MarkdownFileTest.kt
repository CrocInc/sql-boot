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

package com.github.mgramin.sqlboot.tools.files.file.impl

import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.test.assertEquals

class MarkdownFileTest {

    @Test
    @Throws(IOException::class)
    fun parse() {
        val text = """
            |````sql
            |select u.username     as "@schema"
            |     , u.user_id      as "user_id"
            |     , u.created      as "created"
            |  from all_users u
            | order by u.username
            |````
            |
            |### row_count
            |
            |````sql
            |select count(1)
            |  from all_users u
            | order by u.username
            |````
            |""".trimMargin()
        val map = MarkdownFile("test.md", text).content()
//        assertEquals(arrayListOf("", "row_count"), map.keys.toList())
        assertEquals(map[0], """select u.username     as "@schema"
                                |     , u.user_id      as "user_id"
                                |     , u.created      as "created"
                                |  from all_users u
                                | order by u.username""".trimMargin())

        assertEquals(map[1], """select count(1)
                                         |  from all_users u
                                         | order by u.username""".trimMargin())
    }
}