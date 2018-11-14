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


import static com.freitas.cassandra.metrics.Constants.DEFAULT_GRAPHITE_PREFIX;

import java.io.PrintStream;


/**
 * Sample visitor that writes tab-seperated output to a {@link PrintStream}.
 * 
 * @author eevans
 */
public class TsvVisitor implements SampleVisitor {

    private final PrintStream stream;
    private final String prefix;

    public TsvVisitor() {
        this(System.out, DEFAULT_GRAPHITE_PREFIX);
    }

    public TsvVisitor(PrintStream stream, String prefix) {
        this.stream = stream;
        this.prefix = prefix;
    }

    /** {@inheritDoc} */
    @Override
    public void visit(JmxSample jmxSample) {
        this.stream.printf(
                "%s %s %s%n",
                CarbonVisitor.metricName(jmxSample, this.prefix),
                jmxSample.getValue(),
                jmxSample.getTimestamp());
    }

}
