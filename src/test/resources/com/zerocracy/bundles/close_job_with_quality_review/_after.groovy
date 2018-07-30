/*
 * Copyright (c) 2016-2018 Zerocracy
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
package com.zerocracy.bundles.close_job_with_quality_review

import com.jcabi.xml.XML
import com.zerocracy.Farm
import com.zerocracy.Project
import com.zerocracy.pm.cost.Rates
import com.zerocracy.pm.scope.Wbs
import com.zerocracy.pmo.Awards
import com.zerocracy.pmo.Debts
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

def exec(Project project, XML xml) {
  Wbs wbs = new Wbs(project).bootstrap()
  MatcherAssert.assertThat(
    wbs.iterate(),
    Matchers.not(Matchers.contains(Matchers.equalTo('gh:test/test#1')))
  )
  Farm farm = binding.variables.farm
  String coder = 'coder'
  MatcherAssert.assertThat(
    'Incorrect QA bonus for DEV job',
    new Awards(farm, coder).bootstrap().total(),
    Matchers.equalTo(35)
  )
  // @todo #1395:30min This assertion fails because Wbs.role(job, role)
  //  is not working in `add_job_to_wbs` groovy script for this test case.
  //  We need to understand why it fails and fix it.
  String reviewer = 'reviewer'
  MatcherAssert.assertThat(
    'Incorrect QA bonus for REV job',
    new Awards(farm, reviewer).bootstrap().total(),
    Matchers.equalTo(20)
  )
  MatcherAssert.assertThat(
    new Debts(farm).bootstrap().amount(coder),
    Matchers.comparesEqualTo(
      new Rates(project).bootstrap().rate(coder).mul(30).mul(116) / 100 / 60
    )
  )
  MatcherAssert.assertThat(
    new Debts(farm).bootstrap().amount(reviewer),
    Matchers.comparesEqualTo(
      new Rates(project).bootstrap().rate(reviewer).mul(15).mul(133) / 100 / 60
    )
  )
}
