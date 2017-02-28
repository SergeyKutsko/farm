/**
 * Copyright (c) 2016-2017 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.stk.pm.in.orders

import com.jcabi.github.Issue
import com.jcabi.log.Logger
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.ClaimOut
import com.zerocracy.radars.github.Job

assume.type('Job assigned in GitHub').exact()

final ClaimIn claim = new ClaimIn(xml)
final Issue issue = new Job.Issue(github, claim.param('job'))
final String login = claim.param('login')
try {
  new Issue.Smart(issue).assign(login)
  new ClaimOut()
    .type('GitHub issue got an assignee')
    .param('login', login)
    .param('repo', issue.repo().coordinates())
    .param('issue', issue.number())
    .postTo(project)
} catch (final AssertionError ex) {
  Logger.warn(
    this, 'Failed to assign @%s to %s#%d: %s',
    login,
    issue.repo().coordinates(),
    issue.number(),
    ex.localizedMessage
  )
}
