/* Copyright 2015 Eric Evans <eevans@wikimedia.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.freitas.cassandra.metrics;

class Constants {
    static final String DEFAULT_JMX_HOST        = "localhost";
    static final int    DEFAULT_JMX_PORT        = 7199;
    static final String DEFAULT_GRAPHITE_HOST   = "localhost";
    static final int    DEFAULT_GRAPHITE_PORT   = 2003;
    static final String DEFAULT_GRAPHITE_PREFIX = "cassandra";
}
