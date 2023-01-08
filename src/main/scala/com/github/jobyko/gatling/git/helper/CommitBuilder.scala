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

package com.github.jobyko.gatling.git.helper

import com.github.jobyko.gatling.git.helper.MockFiles._
import org.eclipse.jgit.api._
import org.eclipse.jgit.lib.Repository
import java.time.LocalDateTime
import scala.util.Random

class CommitBuilder(repository: Repository) {

  val git = new Git(repository)
  val random = new Random()

  def createCommit(numFiles: Int, minContentLength: Int, maxContentLength: Int) {
    Vector.range(0, numFiles).par.foreach { e =>
      val contentLength: Int = minContentLength + random.nextInt((maxContentLength - minContentLength) +1)
      val file: MockFile = MockFileFactory.create("text", contentLength)
      val fileName: String = file.save(repository.getWorkTree.toString)
    }

    git.add.addFilepattern(".").call()

    val uniqueSuffix = s"${LocalDateTime.now}"
    git
      .commit()
      .setMessage(s"Test commit header - $uniqueSuffix\n\nTest commit body - $uniqueSuffix\n")
      .call()
  }
}
