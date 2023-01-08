// Copyright (C) 2019 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.github.jobyko.gatling.git

import com.github.jobyko.gatling.git.protocol.GitProtocol
import com.github.jobyko.gatling.git.request.builder.Git
import io.gatling.core.Predef.{exec, _}
import io.gatling.core.structure.ScenarioBuilder
import java.io._
import org.apache.commons.io.FileUtils

class AllUsersAllRecordsCloneOnlyScenario extends Simulation {

  val gitProtocol = GitProtocol()
  implicit val conf = GatlingGitConfiguration()

  val feeder = Array(
    Map("repo" -> "test"),
    Map("repo" -> "test1"),
    Map("repo" -> "cose")
  ).readRecords

  val allUsersAllCallsScenario: ScenarioBuilder =
    scenario("Git Clone").foreach(feeder, "record") {
      exec(flattenMapIntoAttributes("${record}"))
        .exec(Git.clone("http://localhost:8081/${repo}"))
    }

  setUp(allUsersAllCallsScenario.inject(atOnceUsers(3)))
    .protocols(gitProtocol)

  after {
    FileUtils.deleteDirectory(new File(conf.tmpBasePath))
  }
}
