package org.nakedobjects.xat;

import org.nakedobjects.object.NakedObjectContext;
import org.nakedobjects.object.defaults.LocalReflectionFactory;
import org.nakedobjects.object.defaults.MockObjectManager;
import org.nakedobjects.object.defaults.NakedObjectSpecificationImpl;
import org.nakedobjects.object.defaults.NakedObjectSpecificationLoaderImpl;
import org.nakedobjects.object.defaults.value.Money;
import org.nakedobjects.object.defaults.value.TextString;
import org.nakedobjects.object.reflect.defaults.JavaReflectorFactory;
import org.nakedobjects.object.security.ClientSession;
import org.nakedobjects.object.security.Session;
import org.nakedobjects.object.system.TestClock;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class TestObjectImplTest extends TestCase {

    public static void main(String[] args) {}

    private TestObject elementOne;
    private TestObject elementThree;

    private TestNaked[] multipleParameters;
    private MockObjectManager om;
    private TestObject singleParameter;
    private TestObject target;
    private TestObjectExample targetObject;

    protected void setUp() throws Exception {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.OFF);

        new TestClock();

        om = MockObjectManager.setup();
        new NakedObjectSpecificationLoaderImpl();
        NakedObjectSpecificationImpl.setReflectionFactory(new LocalReflectionFactory());
        NakedObjectSpecificationImpl.setReflectorFactory(new JavaReflectorFactory());

        NakedObjectContext context = new NakedObjectContext(om);

        Session session = ClientSession.getSession();

        //        Session.getSession().setSecurityContext(context);

        targetObject = new TestObjectExample();
        targetObject.setContext(context);
        //     context = null; //tmp
        TestObjectFactory factory = new DefaultTestObjectFactory();
        target = factory.createTestObject(session, targetObject);

        singleParameter = factory.createTestObject(session, new TestObjectExample());
        TestObject parameter2 = factory.createTestObject(session, new TestObjectExample());
        TestValue valueParameter = factory.createParamerTestValue(new TextString("a value"));
        multipleParameters = new TestNaked[] { singleParameter, parameter2, valueParameter };

        TestElement element = new TestElement("one");
        targetObject.getCollection().add(element);
        elementOne = factory.createTestObject(session, element);
        element = new TestElement("two");
        targetObject.getCollection().add(element);
        element = new TestElement("three");
        targetObject.getCollection().add(element);
        elementThree = factory.createTestObject(session, element);
    }

    protected void tearDown() throws Exception {
        ClientSession.end();
        om.shutdown();
        super.tearDown();
    }

    public void testAssertActionExists() {
        target.assertActionExists("One Default");
        target.assertActionExists("Three Invisible");
        target.assertActionExists("Two Default", singleParameter);
        target.assertActionExists("Seven", multipleParameters);
        try {
            target.assertActionExists("Non existant");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertActionInvisible() {
        target.assertActionInvisible("Three Invisible");
        target.assertActionInvisible("nineInvisible", singleParameter);
        target.assertActionInvisible("eight invisible", multipleParameters);

        try {
            target.assertActionInvisible("One Default");
            fail();
        } catch (AssertionFailedError expected) {}
    }

    public void testAssertActionUnusable() {
        target.assertActionUnusable("Four Unusable");
        target.assertActionUnusable("Six Unusable", singleParameter);
        target.assertActionUnusable("ten  Unusable", multipleParameters);
    }

    public void testAssertActionUsable() {
        target.assertActionUsable("One Default");
        target.assertActionExists("Two Default", singleParameter);
        target.assertActionUsable("Seven", multipleParameters);
    }

    public void testAssertActionVisible() {
        target.assertActionVisible("One Default");
        target.assertActionVisible("Two Default", singleParameter);
        target.assertActionVisible("Seven", multipleParameters);
    }

    public void testAssertFieldContainsElement() {
        target.assertFieldContains("collection", "one");
        try {
            target.assertFieldContains("collection", "four");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFieldEntryInvalid() {
        target.assertFieldEntryInvalid("amount", "-3.0");
        try {
            target.assertFieldEntryInvalid("amount", "7.0");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFieldEntryNotParseable() {
        target.assertFieldEntryCantParse("amount", "xxx");
        try {
            target.assertFieldEntryCantParse("amount", "-3.0");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFieldExists() {
        target.assertFieldExists("One Modifiable");
        target.assertFieldExists("Three Invisible");
        try {
            target.assertFieldExists("Nonexistant");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFieldInvisible() {
        target.assertFieldInvisible("Three Invisible");
        try {
            target.assertFieldInvisible("One Modifiable");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFieldModifiable() {
        target.assertFieldModifiable("One Modifiable");
        try {
            target.assertFieldModifiable("Two Unmodifiable");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFieldUnmodifiable() {
        target.assertFieldUnmodifiable("Two Unmodifiable");
        try {
            target.assertFieldUnmodifiable("One Modifiable");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFieldVisible() {
        target.assertFieldVisible("One Modifiable");
        try {
            target.assertFieldVisible("Three Invisible");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFirstElementInField() {
        target.assertFirstElementInField("collection", elementOne);
        try {
            target.assertFirstElementInField("collection", elementThree);
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertFirstElementInFieldByTitle() {
        target.assertFirstElementInField("collection", "one");
        try {
            target.assertFirstElementInField("collection", "thtree");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertLastElementInField() {
        target.assertLastElementInField("collection", elementThree);
        try {
            target.assertLastElementInField("collection", elementOne);
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssertLasttElementInFieldByTitle() {
        target.assertLastElementInField("collection", "three");
        try {
            target.assertLastElementInField("collection", "one");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testAssociate() {
        target.associate("Four Default", singleParameter);
        assertEquals(singleParameter.toString(), targetObject.result());
    }

    public void testCantAssociate() {
        try {
            target.associate("Five Unmodifiable", singleParameter);
            fail();
        } catch (NakedAssertionFailedError expected) {}

        try {
            target.associate("Six Invisible", singleParameter);
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testCantClearAssociation() {
        try {
            target.clearAssociation("Five Unmodifiable");
            fail();
        } catch (NakedAssertionFailedError expected) {}

        try {
            target.clearAssociation("Six Invisible");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testCantInvokeAction() {

        try {
            target.invokeAction("Four Unusable");
            fail();
        } catch (NakedAssertionFailedError e) {}

        try {
            target.invokeAction("Six Unusable", singleParameter);
            fail();
        } catch (NakedAssertionFailedError e) {}

        try {
            target.invokeAction("ten Unusable", multipleParameters);
            fail();
        } catch (NakedAssertionFailedError e) {}
    }

    public void testCantSeeAction() {
        try {
            target.invokeAction("Three Invisible");
            fail();
        } catch (NakedAssertionFailedError e) {}

        try {
            target.invokeAction("Five Invisible", singleParameter);
            fail();
        } catch (NakedAssertionFailedError e) {}

        try {
            target.invokeAction("EIGHT Invisible", multipleParameters);
            fail();
        } catch (NakedAssertionFailedError e) {}
    }

    public void testCantSetValueFields() {
        try {
            target.fieldEntry("Two Unmodifiable", "text entry");
            fail();
        } catch (NakedAssertionFailedError expected) {}

        try {
            target.fieldEntry("Three Invisible", "text entry");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testClearAssociation() {
        target.clearAssociation("Four Default");
    }

    public void testGetFieldElement() {
        TestObject fld = target.getField("collection", "three");

        assertEquals(elementThree, fld);
    }

    public void testInvalidFieldEntry() {
        try {
            target.fieldEntry("amount", "7.0");
            target.fieldEntry("amount", "-3.0");
            fail();
        } catch (IllegalActionError expected) {}
        Money amount = ((Money) target.getField("amount").getForObject());
        assertEquals(7.0, amount.doubleValue(), 0.0);
    }

    public void testInvokeAction() {
        target.invokeAction("One Default");
        assertEquals("one", targetObject.result());

        target.invokeAction("Two Default", singleParameter);
        assertEquals("two", targetObject.result());

        target.invokeAction("Seven", multipleParameters);
        assertEquals("a value", targetObject.result());
    }

    public void testSetValueFields() {
        target.fieldEntry("One Modifiable", "text entry");
    }

    public void testTestAssociate() {
        target.testField("Four Default", singleParameter);
    }

    public void testTestAssociateButCantBeChanged() {
        try {
            target.testField("Five Unmodifiable", singleParameter);
            fail();
        } catch (NakedAssertionFailedError expected) {}

        try {
            target.testField("Six Invisible", singleParameter);
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }

    public void testValueTestFields() {
        target.testField("One Modifiable", "text entry");
        target.testField("One Modifiable", "text entry", "text entry");
    }

    public void testValueTestFieldsThatCantBeChanged() {
        try {
            target.testField("Two Unmodifiable", "text entry");
            fail();
        } catch (NakedAssertionFailedError expected) {}

        try {
            target.testField("Two Unmodifiable", "text entry", "text entry");
            fail();
        } catch (NakedAssertionFailedError expected) {}

        try {
            target.testField("Three Invisible", "text entry");
            fail();
        } catch (NakedAssertionFailedError expected) {}

        try {
            target.testField("Three Invisible", "text entry", "text entry");
            fail();
        } catch (NakedAssertionFailedError expected) {}
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2004 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */