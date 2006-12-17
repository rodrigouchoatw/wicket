/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.protocol.http.portlet;

/*
 * $Id: MockWebApplicationTest.java 6069 2006-06-06 18:23:50Z jannehietamaki $
 * $Revision: 6069 $ $Date: 2006-06-06 21:23:50 +0300 (Tue, 06 Jun 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import junit.framework.Assert;
import junit.framework.TestCase;
import wicket.markup.html.link.Link;
import wicket.util.diff.DiffUtil;

/**
 * Simple tester that demonstrates the mock portlet tester code (and
 * checks that it is working)
 * 
 * @author Janne Hietam&auml;ki (jannehietamaki)
 */
public class MockPortletApplicationTest extends TestCase
{

	private MockPortletApplication application;

	/**
	 * Create the test.
	 * 
	 * @param name
	 *            The test name
	 */
	public MockPortletApplicationTest(String name)
	{
		super(name);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		application = new MockPortletApplication(null);
		application.setHomePage(MockPortletPage.class);
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage() throws Exception
	{	
		application.createRenderRequest();
		application.processRenderRequestCycle(application.createRenderRequestCycle());

		// Validate the document
		String document = application.getPortletResponse().getDocument();
		DiffUtil.validatePage(document, this.getClass(), "MockPortletPage_expectedResult.html",true);

		// Inspect the page & model
		MockPortletPage p = (MockPortletPage)application.getLastRenderedPage();
		Assert.assertEquals("Link should have been clicked 0 times", 0, p.getLinkClickCount());
	}

	/**
	 * @throws Exception
	 */
	public void testClickLink() throws Exception
	{		
		application.createRenderRequest();
		application.processRenderRequestCycle(application.createRenderRequestCycle());

		application.createActionRequest();
		PortletActionRequestCycle requestCycle=application.createActionRequestCycle();
		MockPortletPage p = (MockPortletPage)application.getLastRenderedPage();
		Link link = (Link)p.get("actionLink");
		application.getPortletRequest().setRequestToComponent(link);
		application.processActionRequestCycle(requestCycle);

		application.createRenderRequest();
		application.processRenderRequestCycle(application.createRenderRequestCycle());

		// Validate the document
		String document = application.getPortletResponse().getDocument();
		DiffUtil.validatePage(document, this.getClass(), "MockPortletPage_expectedResult2.html",true);

		// Inspect the page & model
		p = (MockPortletPage)application.getLastRenderedPage();
		Assert.assertEquals("Link should have been clicked 1 time", 1, p.getLinkClickCount());
	}	 
}