package org.inurl.spring.ext.bind;

import com.google.common.collect.ImmutableMap;
import org.inurl.spring.ext.bind.annotation.ModelRequestParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author raylax
 */
public class ModelRequestParamBinderTest {

    private MockHttpServletRequest request;

    @BeforeEach
    public void setup() {
        this.request = new MockHttpServletRequest();
        request.addParameter("a", "1");
        request.addParameter("b", "2");
        request.addParameter("e", "3");
        request.addParameter("y", "6666");
    }

    @Test
    public void testMapping() {
        TestModel target = new TestModel();
        ImmutableMap<String, String> mapping = ImmutableMap.of(
                "b", "c",
                "y", "z"
        );
        ServletRequestDataBinder binder = new ModelRequestParamBinder(target, "", mapping);
        binder.bind(request);
        assertThat(target.getA()).isEqualTo("1");
        assertThat(target.getC()).isEqualTo("2");
        assertThat(target.getE()).isEqualTo(3);
        assertThat(target.getZ()).isEqualTo(6666);
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