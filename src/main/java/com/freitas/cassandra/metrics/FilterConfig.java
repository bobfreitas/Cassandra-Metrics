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

import java.util.Collection;
import java.util.Collections;

public class FilterConfig {
    private Collection<String> blacklist;
    private Collection<String> whitelist;

    public Collection<String> getBlacklist() {
        return (blacklist != null) ? blacklist : Collections.<String>emptyList();
    }

    public void setBlacklist(Collection<String> blacklist) {
        this.blacklist = blacklist;
    }

    public Collection<String> getWhitelist() {
        return (whitelist != null) ? whitelist : Collections.<String>emptyList();
    }

    public void setWhitelist(Collection<String> whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public String toString() {
        return "FilterConfig [blacklist=" + blacklist + ", whitelist=" + whitelist + "]";
    }

}
