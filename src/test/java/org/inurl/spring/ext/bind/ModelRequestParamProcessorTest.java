package org.inurl.spring.ext.bind;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author raylax
 */
public class ModelRequestParamProcessorTest {

    private NativeWebRequest request;
    private ModelAttributeMethodProcessor processor;
    private ModelAndViewContainer container;
    private MethodParameter paramNamedValidModelAttr;

    @BeforeEach
    public void setup() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("a", "1");
        request.addParameter("b", "2");
        request.addParameter("e", "3");
        request.addParameter("y", "6666");
        this.request = new ServletWebRequest(request);
        this.container = new ModelAndViewContainer();
        this.processor = new ModelRequestParamProcessor();
        Method method = ModelAttributeHandler.class.getDeclaredMethod("modelAttribute", TestModel.class);
        this.paramNamedValidModelAttr = new SynthesizingMethodParameter(method, 0);
    }

    @Test
    public void testBindRequestParameters() throws Exception {
        String name = "testModel";
        TestModel target = new TestModel();
        this.container.addAttribute(name, target);
        ImmutableMap<String, String> mapping = ImmutableMap.of(
                "b", "c",
                "y", "z"
        );
        ModelRequestParamBinder dataBinder = new ModelRequestParamBinder(target, "", mapping);
        WebDataBinderFactory factory = mock(WebDataBinderFactory.class);
        when(factory.createBinder(this.request, target, name)).thenReturn(dataBinder);
        this.processor.resolveArgument(this.paramNamedValidModelAttr, this.container, this.request, factory);
        assertThat(target.getA()).isEqualTo("1");
        assertThat(target.getC()).isEqualTo("2");
        assertThat(target.getE()).isEqualTo(3);
        assertThat(target.getZ()).isEqualTo(6666);
    }

    private static class ModelAttributeHandler {

        public void modelAttribute(TestModel testModel) {
        }
    }


    private static class TestModel {
        private String a;

        @ModelRequestParam("b")
        private String c;

        private int e;

        @ModelRequestParam(name = "y")
        private int z;

        public void setZ(int z) {
            this.z = z;
        }

        public int getZ() {
            return z;
        }

        public void setE(int e) {
            this.e = e;
        }

        public int getE() {
            return e;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }
    }

}