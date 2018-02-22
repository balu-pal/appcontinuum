package io.barinek.continuum.timesheets

import com.fasterxml.jackson.databind.ObjectMapper
import io.barinek.continuum.discovery.DiscoveryClient
import io.barinek.continuum.restsupport.RestTemplate
import org.apache.http.message.BasicNameValuePair

open class ProjectClient(val mapper: ObjectMapper, val template: RestTemplate) {
    open fun getProject(projectId: Long): ProjectInfo? {
        val endpoint = DiscoveryClient(mapper, template).getUrl("registration") ?: return null
        val response = template.get("$endpoint/project", "application/vnd.appcontinuum.v2+json", BasicNameValuePair("projectId", projectId.toString()))
        when {
            response.isBlank() -> return null

            else -> return mapper.readValue(response, ProjectInfo::class.java)
        }
    }
}