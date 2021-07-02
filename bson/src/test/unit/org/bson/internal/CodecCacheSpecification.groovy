/*
 * Copyright 2008-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bson.internal

import org.bson.codecs.BooleanCodec
import org.bson.codecs.MinKeyCodec
import org.bson.codecs.configuration.CodecConfigurationException
import org.bson.types.MinKey
import spock.lang.Specification

class CodecCacheSpecification extends Specification {

    def 'should return the cached codec if a codec for the class exists'() {
        when:
        def codec = new MinKeyCodec()
        def cache = new CodecCache()
        cache.put(MinKey, codec)

        then:
        cache.getOrThrow(MinKey).is(codec)
    }

    def 'should throw if codec for class does not exist'() {
        when:
        def cache = new CodecCache()
        cache.getOrThrow(MinKey)

        then:
        thrown(CodecConfigurationException)
    }

    def 'should throw if cached codec for class is null'() {
        when:
        def cache = new CodecCache()
        cache.put(MinKey, null)
        cache.getOrThrow(MinKey)

        then:
        thrown(CodecConfigurationException)
    }

    def 'should put and return the cached codec if there was no coded in cache'() {
        when:
        def codec = new MinKeyCodec()
        def cache = new CodecCache()
        cache.putIfMissing(MinKey, codec)

        then:
        cache.getOrThrow(MinKey).is(codec)
    }

    def 'should return already cached codec if there was coded in cache'() {
        when:
        def codec = new BooleanCodec()
        def cache = new CodecCache()
        cache.put(MinKey, codec)
        cache.putIfMissing(MinKey, new MinKeyCodec())

        then:
        cache.getOrThrow(MinKey).is(codec)
    }

    def 'should put and return new cached codec if there was coded in cache but it was null'() {
        when:
        def codec = new MinKeyCodec()
        def cache = new CodecCache()
        cache.put(MinKey, null)
        cache.putIfMissing(MinKey, codec)

        then:
        cache.getOrThrow(MinKey).is(codec)
    }
}
