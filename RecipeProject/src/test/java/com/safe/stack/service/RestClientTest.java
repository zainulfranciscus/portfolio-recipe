package com.safe.stack.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.core.util.OrderRetainingMap;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.Mapper;

public class RestClientTest {

    public static void main(String[] args) {

	GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
	ctx.load("classpath:servlet-context.xml");
	RestTemplate restTemplate = ctx.getBean("restTemplate", RestTemplate.class);

	MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
	map.add("field1", "color");
	map.add("field1", "green");
	map.add("field2", "red");

	ListMultimap<String, String> multimap = LinkedListMultimap.create();
	multimap.put("x", "1");
	multimap.put("x", "2");
	multimap.put("y", "3");

	// restTemplate.postForLocation("http://localhost:8080/RecipeProject/restCallConsumer",
	// map,
	// String.class);
	Map<String, Object> map3 = new HashMap<String, Object>();
	map3.put("name", "chris");
	map3.put("island", "faranga");

	XStream xStream = new XStream(new DomDriver());
	xStream.registerConverter(new MapEntryConverter(xStream.getMapper()));
	
	xStream.alias("add", multimap.getClass());
	String xml = xStream.toXML(multimap);
	System.out.println(xml);

    }

    public static class MapEntryConverter extends MapConverter {

	public MapEntryConverter(Mapper mapper) {
	    super(mapper);	   
	}

	public boolean canConvert(Class clazz) {
	    return ListMultimap.class.isAssignableFrom(clazz);
	}

	public void marshal(Object value, HierarchicalStreamWriter writer,
		MarshallingContext context) {

	    ListMultimap<String, String> map = (ListMultimap<String, String>) value;
	    for (String key : map.keys()) {
		writer.startNode(key);
		writer.setValue(map.get(key).get(0));
		writer.endNode();
	    }
	}

	
    }

}