package com.safe.stack.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.Mapper;

public class RestClientTest {

    public static void main(String[] args) {

	GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
	ctx.load("classpath:servlet-context.xml");
	RestTemplate restTemplate = ctx.getBean("restTemplate", RestTemplate.class);

	ListMultimap<String, String> multimap = LinkedListMultimap.create();
	multimap.put("x", "1");
	multimap.put("x", "2");
	multimap.put("y", "3");

	XStream xStream = new XStream(new DomDriver());
	xStream.registerConverter(new MapEntryConverter(xStream.getMapper()));

	xStream.alias("add", multimap.getClass());
	String xml = xStream.toXML(multimap);
	System.out.println(xml);

//
//	List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//	Jaxb2RootElementHttpMessageConverter jaxbMessageConverter = new Jaxb2RootElementHttpMessageConverter();
//	List<MediaType> mediaTypes = new ArrayList<MediaType>();
//	mediaTypes.add(MediaType.TEXT_XML);
//	jaxbMessageConverter.setSupportedMediaTypes(mediaTypes);
//	messageConverters.add(jaxbMessageConverter);
//	restTemplate.setMessageConverters(messageConverters);
	
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_XML);
	
	HttpEntity<String> entity = new HttpEntity<String>(xml,headers);
	
	restTemplate.postForLocation("http://localhost:8080/RecipeProject/restCallConsumer", entity,HttpEntity.class);
	
//	restTemplate.postForObject("http://localhost:8080/RecipeProject/restCallConsumer", xml,
//		String.class);

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