package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.annotations.AnnotationHandler;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class AnnotationHandlerTests extends BaseTest {
	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void annotationHandler1( GameTestHelper helper ) {
		AnnotationHandler handler = new AnnotationHandler( "com.mlib.tests", TestAnnotation1.class );
		assertThat( helper, handler.getInstances().size(), 1, ()->"AnnotationHandler does not load TestClass1 properly" );
		helper.succeed();
	}

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void annotationHandler2( GameTestHelper helper ) {
		AnnotationHandler handler = new AnnotationHandler( "com.mlib.tests", TestAnnotation2.class );
		assertThat( helper, handler.getInstances().size(), 1, ()->"AnnotationHandler does not load TestClass2 because of missing explicit constructor" );
		helper.succeed();
	}

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void annotationHandler3( GameTestHelper helper ) {
		AnnotationHandler handler = new AnnotationHandler( "com.mlib.tests", TestAnnotation3.class );
		assertThat( helper, handler.getInstances().size(), 2, ()->"AnnotationHandler does not TestClass3A and TestClass3B properly" );
		assertThat( helper, handler.getInstances( TestClass3A.class ).size(), 2, ()->"AnnotationHandler does not return valid list of TestClass3A instances" );
		assertThat( helper, handler.getInstances( TestClass3B.class ).size(), 1, ()->"AnnotationHandler does not return valid list of TestClass3B instances" );
		helper.succeed();
	}

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void annotationHandler4( GameTestHelper helper ) {
		AnnotationHandler handler = new AnnotationHandler( "com.mlib.tests", TestAnnotation4.class );
		assertThat( helper, handler.getInstances().size(), 2, ()->"AnnotationHandler does not handle inner classes properly" );
		helper.succeed();
	}

	public AnnotationHandlerTests() {
		super( AnnotationHandlerTests.class );
	}

	@Target( ElementType.TYPE )
	@Retention( RetentionPolicy.RUNTIME )
	public @interface TestAnnotation1 {}

	@TestAnnotation1
	public static class TestClass1 {
		public TestClass1() {}
	}

	@Target( ElementType.TYPE )
	@Retention( RetentionPolicy.RUNTIME )
	public @interface TestAnnotation2 {}

	@TestAnnotation2
	public static class TestClass2 {}

	@Target( ElementType.TYPE )
	@Retention( RetentionPolicy.RUNTIME )
	public @interface TestAnnotation3 {}

	@TestAnnotation3
	public static class TestClass3A {
		public TestClass3A() {}
	}

	@TestAnnotation3
	public static class TestClass3B extends TestClass3A {
		public TestClass3B() {}
	}

	@Target( ElementType.TYPE )
	@Retention( RetentionPolicy.RUNTIME )
	public @interface TestAnnotation4 {}

	@TestAnnotation4
	public static class TestClass4A {
		public TestClass4A() {}

		@TestAnnotation4
		public static class TestClass4B {
			public TestClass4B() {}
		}
	}
}
