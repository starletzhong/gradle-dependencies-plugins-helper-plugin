package cn.bestwu.gdph

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.intentions.base.PsiElementPredicate
import org.jetbrains.plugins.groovy.lang.psi.util.GrStringUtil

class MethodNotationAddRepositoriesIntention : AbstractAddRepositoriesIntention() {

    override fun processIntention(element: PsiElement, project: Project, editor: Editor) {
        var stringNotation = GrStringUtil.removeQuotes(element.text)
        if (stringNotation.count { it == ':' } == 1) {
            stringNotation += ":"
        }
        processIntention(SearchParam(stringNotation), project, element)
    }

    override fun getElementPredicate(): PsiElementPredicate {
        return PsiElementPredicate { element ->
            val text = GrStringUtil.removeQuotes(element.text)
            GradleDependenciesCompletionContributor.IN_METHOD_DEPENDENCY_NOTATION.accepts(element) && !text.contains("['\"]".toRegex()) && text.count { it == ':' } > 0
        }
    }


}