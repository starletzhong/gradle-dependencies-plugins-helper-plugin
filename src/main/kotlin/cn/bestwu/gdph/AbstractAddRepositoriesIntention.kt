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

package cn.bestwu.gdph

import cn.bestwu.gdph.search.ArtifactInfo
import cn.bestwu.gdph.search.JcenterSearcher
import cn.bestwu.gdph.search.SearchParam
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.plugins.groovy.intentions.base.Intention
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import java.util.*

abstract class AbstractAddRepositoriesIntention : Intention() {

    private fun findClosure(psiFile: PsiFile, expressionName: String): GrMethodCall? {
        val methodCalls = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, GrMethodCall::class.java)
        return methodCalls.find { it.invokedExpression.text == expressionName }
    }

    protected fun processIntention(searchParam: SearchParam, project: Project, element: PsiElement) {
        val result: LinkedHashSet<ArtifactInfo> = JcenterSearcher.search(searchParam, project, linkedSetOf())
        if (result.isNotEmpty()) {
            val psiFile = element.containingFile
            val repositoriesClosure = findClosure(psiFile, "repositories")?.closureArguments?.first()
            val factory = GroovyPsiElementFactory.getInstance(project)
            val repo = if (result.first().isSpecifiedRepo()) "\t\tmaven { url '${result.first().repo()}' }" else "\t\tjcenter()"
            if (repositoriesClosure == null) {
                val dependenciesElement = findClosure(psiFile, "dependencies")
                psiFile.addBefore(factory.createStatementFromText("repositories {\n$repo\n}"), dependenciesElement)
                psiFile.addBefore(factory.createLineTerminator(2), dependenciesElement)
            } else {
                if (repositoriesClosure.text.contains(if (result.first().isSpecifiedRepo()) result.first().repo() else "jcenter")) {
                    show(project, "repository:\n$repo\n already in repositories", type = NotificationType.WARNING)
                } else {
                    repositoriesClosure.addStatementBefore(factory.createStatementFromText(repo), null)
                }
            }
        }
    }


    override fun getText(): String {
        return "Add specified repository to repositories"
    }

    override fun getFamilyName(): String {
        return "Add specified repository to repositories"
    }
}
