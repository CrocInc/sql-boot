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

import com.github.mgramin.sqlboot.tools.files.file.File
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Heading
import org.commonmark.node.Text
import org.commonmark.parser.Parser
import java.util.*

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: d5d9fbccca9519bf74e3b6add53e46104ffa5931 $
 * @since 0.1
 */
class MarkdownFile(private val name: String, private val text: String) : File {

    override fun name() = name

    override fun content(): List<String> {
        val visitor = CustomVisitor()
        Parser.builder().build().parse(text).accept(visitor)
        return visitor
                .getMap()
                .asSequence()
                .ifEmpty { mapOf("" to "").asSequence() }
                .map { it.value }
                .toList()
    }

    class CustomVisitor : AbstractVisitor() {

        private var currentTag: String = ""
        private val map = LinkedHashMap<String, String>()
        private var key = 1

        override fun visit(text: Text) {
            if (text.parent is Heading && (text.parent as Heading).level >= 3) {
                currentTag = text.literal
            }
        }

        override fun visit(fencedCodeBlock: FencedCodeBlock) {
            if (fencedCodeBlock.fenceLength == 4) {
                map[key++.toString()] = fencedCodeBlock.literal.trim()
            }
        }

        fun getMap() = map

    }

}
