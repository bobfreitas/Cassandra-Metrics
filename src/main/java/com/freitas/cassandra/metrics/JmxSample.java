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


import static com.google.common.base.Preconditions.checkNotNull;

import javax.management.ObjectName;


public class JmxSample {

    public static enum Type {
        CASSANDRA_METRIC, CASSANDRA_NET, JVM;
    }

    private final Type type;
    private final ObjectName objectName;
    private final String name;
    private final Object value;
    private final Number timestamp;

    public JmxSample(Type type, ObjectName oName, String metricName, Object value, Number timestamp) {
        this.type = checkNotNull(type, "type argument");
        this.objectName = checkNotNull(oName, "objectName argument");
        this.name = checkNotNull(metricName, "name argument");
        this.value = checkNotNull(value, "value argument");
        this.timestamp = checkNotNull(timestamp, "timestamp argument");
    }

    public Type getType() {
        return this.type;
    }

    public ObjectName getObjectName() {
        return this.objectName;
    }

    public String getMetricName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

    public Number getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return "JmxSample [type="
                + type
                + ", objectName="
                + objectName
                + ", name="
                + name
                + ", value="
                + value
                + ", timestamp="
                + timestamp
                + "]";
    }

}