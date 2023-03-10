package com.idd.dia

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode
import io.kotest.extensions.spring.SpringExtension
import org.springframework.stereotype.Component

@Component
class KotestConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(SpringExtension)
    override val isolationMode: IsolationMode = IsolationMode.InstancePerLeaf
}
