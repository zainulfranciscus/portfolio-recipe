/**
 * Created on Jan 4, 2012
 */
package com.safe.stack.web.controller;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.safe.stack.config.ControllerTestConfig;

/**
 * An abstract class intended to be the superclass for unit test classes that
 * test a controller.
 * 
 * @author Zainul Franciscus
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ControllerTestConfig.class })
@ActiveProfiles("test")
@Ignore
public class AbstractControllerTest {

}
