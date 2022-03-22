/*
 * Copyright 2022 KCodeYT
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

package de.kcodeyt.plotplugin.util;

import lombok.Value;

/**
 * @author Kevims KCodeYT
 */
@Value(staticConstructor = "of")
public class PlotId {

    int x;
    int z;

    public PlotId add(int x, int z) {
        return new PlotId(this.x + x, this.z + z);
    }

    public PlotId subtract(int x, int z) {
        return this.add(-x, -z);
    }

    @Override
    public String toString() {
        return this.x + ";" + this.z;
    }

}
