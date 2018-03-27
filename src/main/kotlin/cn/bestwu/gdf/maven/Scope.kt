/*
 * Copyright (c) 2017 Peter Wu
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

package cn.bestwu.gdf.maven

import javax.xml.bind.annotation.XmlEnum
import javax.xml.bind.annotation.XmlEnumValue

@XmlEnum
enum class Scope {

    @XmlEnumValue("compile")
    COMPILE,
    @XmlEnumValue("provided")
    PROVIDED,
    @XmlEnumValue("runtime")
    RUNTIME,
    @XmlEnumValue("test")
    TEST,
    @XmlEnumValue("system")
    SYSTEM
}