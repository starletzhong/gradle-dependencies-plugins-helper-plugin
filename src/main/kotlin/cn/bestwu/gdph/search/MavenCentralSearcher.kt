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

package cn.bestwu.gdph.search

import cn.bestwu.gdph.quot
import com.intellij.openapi.project.Project
import java.util.*

/**
 *
 * @author Peter Wu
 * @since
 */
object MavenCentralSearcher : ArtifactSearcher() {

    override val cache: Boolean
        get() = true
    override val key: String
        get() = "maven:"

    fun artifactInfo(groupId: String, artifactId: String, version: String = ""): ArtifactInfo = ArtifactInfo(groupId, artifactId, version, "mavenCentral")

    override fun doSearch(searchParam: SearchParam, project: Project, result: LinkedHashSet<ArtifactInfo>): LinkedHashSet<ArtifactInfo> {
        val url = "http://search.maven.org/solrsearch/select?q=${searchParam.toMq()}&rows=50&core=gav&wt=json"
        val connection = getConnection(url)
        val stream = getResponse(connection, project) ?: return result
        regex.findAll(stream.bufferedReader().readText()).forEach {
            if (searchParam.fg && it.groupValues[1] != searchParam.groupId)
                return@forEach
            val artifactInfo = artifactInfo(it.groupValues[1], if (searchParam.artifactId.isEmpty() && !searchParam.fg && searchParam.groupId.isNotEmpty() && searchParam.groupId != it.groupValues[1]) "" else it.groupValues[2])
            if (artifactInfo.id == searchParam.toId() && artifactInfo.artifactId.isNotEmpty()) {
                artifactInfo.version = it.groupValues[3]
                result.add(artifactInfo)
            } else if (!searchParam.fa) {
                result.add(artifactInfo)
            }
        }
        return result
    }

    override fun handleEmptyResult(searchParam: SearchParam, project: Project, result: LinkedHashSet<ArtifactInfo>): LinkedHashSet<ArtifactInfo> {
        return JcenterSearcher.search(searchParam, project, result)
    }

    override fun doSearchByClassName(searchParam: ClassNameSearchParam, project: Project, result: LinkedHashSet<ArtifactInfo>): LinkedHashSet<ArtifactInfo> {
        val url = "http://search.maven.org/solrsearch/select?q=${searchParam.k}:$quot${searchParam.q}$quot&core=gav&rows=1000&wt=json"
        val connection = getConnection(url)
        val stream = getResponse(connection, project) ?: return result
        regex.findAll(stream.bufferedReader().readText()).forEach {
            val artifactInfo = artifactInfo(it.groupValues[1], it.groupValues[2], it.groupValues[3])
            val exist = result.find { it.id == artifactInfo.id }
            if (exist != null) {
                if (compareVersion(exist.version, artifactInfo.version) < 0) {
                    exist.version = artifactInfo.version
                }
            } else {
                result.add(artifactInfo)
            }
        }
        return result
    }

}