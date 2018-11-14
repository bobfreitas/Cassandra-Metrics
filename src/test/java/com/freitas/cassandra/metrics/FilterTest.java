/* Copyright 2015 Eric Evans <eevans@wikimedia.org> and Wikimedia Foundation
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.freitas.cassandra.metrics.Filter;
import com.freitas.cassandra.metrics.FilterConfig;


public class FilterTest {

    @Test
    public void test() {

        Yaml yaml = new Yaml(new Constructor(FilterConfig.class));
        FilterConfig config = (FilterConfig)yaml.load(getClass().getResourceAsStream("/filter-test.yaml"));
        Filter filter = new Filter(config);

        assertThat(filter.accept("acceptable.value"), is(true));
        assertThat(filter.accept("blacklisted.metric.1MinuteRate"), is(false));
        assertThat(filter.accept("blacklisted.metric.99Percentile"), is(false));

        // This one should be white listed
        assertThat(filter.accept("blacklisted.wanted.metric.99Percentile"), is(true));

    }

}
