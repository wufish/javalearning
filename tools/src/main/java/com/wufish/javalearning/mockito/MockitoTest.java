package com.wufish.javalearning.mockito;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import lombok.Data;

/**
 * The type Mockito test.
 *
 * @Author wzj
 * @Create time : 2019/04/26 12:51
 * @Description:
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {
    /**
     * 验证行为
     */
    @Test
    public void verify_behaviour() {

        // 模拟创建一个List对象
        List mockList = Mockito.mock(List.class);
        // 使用mock的对象
        mockList.add(1);
        mockList.clear();
        // 验证add(1)和clear()行为是否发生
        Mockito.verify(mockList).add(1);
        Mockito.verify(mockList).clear();
    }

    /**
     * When then return.
     */
    @Test
    public void when_thenReturn() {
        // mock一个Iterator类
        Iterator mockIter = Mockito.mock(Iterator.class);
        // 预设当iterator调用next()时第一次返回hello，第n次都返回world
        Mockito.when(mockIter.next()).thenReturn("hello").thenReturn("world");
        // 使用mock的对象
        String result = mockIter.next() + " " + mockIter.next() + " " + mockIter.next();
        // 验证结果
        Assert.assertEquals("hello world world", result);
    }

    /**
     * When then throw.
     *
     * @throws IOException the io exception
     */
    @Test(expected = IOException.class)
    public void when_thenThrow() throws IOException {
        OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
        OutputStreamWriter writer = new OutputStreamWriter(mockOutputStream);
        // 预设当流关闭时抛出异常
        Mockito.doThrow(new IOException()).when(mockOutputStream).close();
        mockOutputStream.close();
    }

    /**
     * 在创建mock对象时，有的方法我们没有进行stubbing，所以调用时会放回Null这样在进行操作是很可能抛出NullPointerException。
     * 如果通过RETURNS_SMART_NULLS参数创建的mock对象在没有调用stubbed方法时会返回SmartNull。例如：返回类型是String，会返回"";
     * 是int 会返回0；是List，会返回空的List。另外，在控制台窗口中可以看到SmartNull的友好提示。
     */
    @Test
    public void returnsSmartNullsTest() {
        List mock = Mockito.mock(List.class, Mockito.RETURNS_SMART_NULLS);
        System.out.println(mock.get(0));

        // 使用RETURNS_SMART_NULLS参数创建的mock对象，不会抛出NullPointerException异常。
        // 另外控制台窗口会提示信息“SmartNull returned by unstubbed get() method on mock”
        System.out.println(mock.toArray().length);
    }

    /**
     * Deepstubs test.
     */
    @Test
    public void deepstubsTest() {
        Account account = Mockito.mock(Account.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(account.getRailwayTicket().getDestination()).thenReturn("Beijing");
        account.getRailwayTicket().getDestination();
        Mockito.verify(account.getRailwayTicket()).getDestination();
        Assert.assertEquals("Beijing", account.getRailwayTicket().getDestination());
    }

    /**
     * RETURNS_DEEP_STUBS参数程序会自动进行mock所需的对象，方法deepstubsTest和deepstubsTest2是等价的
     */
    @Test
    public void deepstubsTest2() {
        Account account = Mockito.mock(Account.class);
        RailwayTicket railwayTicket = Mockito.mock(RailwayTicket.class);
        Mockito.when(account.getRailwayTicket()).thenReturn(railwayTicket);
        Mockito.when(railwayTicket.getDestination()).thenReturn("Beijing");

        account.getRailwayTicket().getDestination();
        Mockito.verify(account.getRailwayTicket()).getDestination();
        Assert.assertEquals("Beijing", account.getRailwayTicket().getDestination());
    }

    /**
     * The type Railway ticket.
     */
    @Data
    public class RailwayTicket {
        private String destination;
    }

    /**
     * The type Account.
     */
    @Data
    public class Account {
        private RailwayTicket railwayTicket;
    }

    /**
     * 模拟方法体抛出异常
     */
    @Test(expected = RuntimeException.class)
    public void doThrow_when() {
        List list = Mockito.mock(List.class);
        Mockito.doThrow(new RuntimeException()).when(list).add(1);
        list.add(1);
    }

    /**
     * 使用注解来快速模拟
     */
    @Mock
    private List mockList;

    public MockitoTest() {
        // 初始化mock的代码, 或者 @RunWith(MockitoJUnitRunner.class)
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shorthand() {
        mockList.add(1);
        Mockito.verify(mockList).add(1);
    }

    /**
     * 参数匹配
     */
    @Test
    public void with_arguments() {
        Comparable comparable = Mockito.mock(Comparable.class);
        //预设根据不同的参数返回不同的结果
        Mockito.when(comparable.compareTo("Test")).thenReturn(1);
        Mockito.when(comparable.compareTo("Omg")).thenReturn(2);
        Assert.assertEquals(1, comparable.compareTo("Test"));
        Assert.assertEquals(2, comparable.compareTo("Omg"));
        //对于没有预设的情况会返回默认值
        Assert.assertEquals(0, comparable.compareTo("Not stub"));
    }

    @Test
    public void with_unspecified_arguments() {
        List list = Mockito.mock(List.class);
        //匹配任意参数
        Mockito.when(list.get(Matchers.anyInt())).thenReturn(1);
        Mockito.when(list.contains(Matchers.argThat(new IsValid()))).thenReturn(true);
        Assert.assertEquals(1, list.get(1));
        Assert.assertEquals(1, list.get(999));
        TestCase.assertTrue(list.contains(1));
        TestCase.assertTrue(!list.contains(3));
    }

    private class IsValid extends ArgumentMatcher<List> {
        @Override
        public boolean matches(Object o) {
            return (int) o == 1 || (int) o == 2;
        }
    }


    /**
     * 如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配，如下代码：
     */
    @Test
    public void all_arguments_provided_by_matchers() {
        Comparator comparator = Mockito.mock(Comparator.class);
        comparator.compare("nihao", "hello");
        //如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
        Mockito.verify(comparator).compare(Matchers.anyString(), Matchers.eq("hello"));
        //下面的为无效的参数匹配使用
        //verify(comparator).compare(anyString(),"hello");
    }

    /**
     * 自定义参数匹配
     */
    @Test
    public void argumentMatchersTest() {
        //创建mock对象
        List<String> mock = Mockito.mock(List.class);
        //argThat(Matches<T> matcher)方法用来应用自定义的规则，可以传入任何实现Matcher接口的实现类。
        Mockito.when(mock.addAll(Matchers.argThat(new IsListofTwoElements()))).thenReturn(true);

        mock.addAll(Arrays.asList("one", "two", "three"));
        //IsListofTwoElements用来匹配size为2的List，因为例子传入List为三个元素，所以此时将失败。
        Mockito.verify(mock).addAll(Matchers.argThat(new IsListofTwoElements()));
    }

    class IsListofTwoElements extends ArgumentMatcher<List> {
        public boolean matches(Object list) {
            return ((List) list).size() == 2;
        }
    }

    /**
     * 捕获参数来进一步断言
     * 较复杂的参数匹配器会降低代码的可读性，有些地方使用参数捕获器更加合适。
     */
    @Test
    public void capturing_args() {
        PersonDao personDao = Mockito.mock(PersonDao.class);
        PersonService personService = new PersonService(personDao);

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        personService.update(1, "jack");
        Mockito.verify(personDao).update(argument.capture());
        Assert.assertEquals(1, argument.getValue().getId());
        Assert.assertEquals("jack", argument.getValue().getName());
    }

    class Person {
        private int id;
        private String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    interface PersonDao {
        public void update(Person person);
    }

    class PersonService {
        private PersonDao personDao;

        PersonService(PersonDao personDao) {
            this.personDao = personDao;
        }

        public void update(int id, String name) {
            personDao.update(new Person(id, name));
        }
    }

    /**
     * 使用方法预期回调接口生成期望值（Answer结构）
     */
    @Test
    public void answerTest() {
        Mockito.when(mockList.get(Matchers.anyInt())).thenAnswer(new CustomAnswer());
        Assert.assertEquals("hello world:0", mockList.get(0));
        Assert.assertEquals("hello world:999", mockList.get(999));
    }

    private class CustomAnswer implements Answer<String> {
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return "hello world:" + args[0];
        }
    }

    /**
     * 也可使用匿名内部类实现
     */
    @Test

    public void answer_with_callback() {
        //使用Answer来生成我们我们期望的返回
        Mockito.when(mockList.get(Matchers.anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return "hello world:" + args[0];
            }
        });
        Assert.assertEquals("hello world:0", mockList.get(0));
        Assert.assertEquals("hello world:999", mockList.get(999));
    }

    /**
     * 修改对未预设的调用返回默认期望
     */
    @Test
    public void unstubbed_invocations() {
        //mock对象使用Answer来对未预设的调用返回默认期望值
        List mock = Mockito.mock(List.class, new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 999;
            }
        });
        //下面的get(1)没有预设，通常情况下会返回NULL，但是使用了Answer改变了默认期望值
        Assert.assertEquals(999, mock.get(1));
        //下面的size()没有预设，通常情况下会返回0，但是使用了Answer改变了默认期望值
        Assert.assertEquals(999, mock.size());
    }

    /**
     * 用spy监控真实对象
     * Mock不是真实的对象，它只是用类型的class创建了一个虚拟对象，并可以设置对象行为
     * Spy是一个真实的对象，但它可以设置对象行为
     * InjectMocks创建这个类的对象并自动将标记@Mock、
     *
     * @Spy等注解的属性值注入到这个中
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void spy_on_real_objects() {
        List list = new LinkedList();
        List spy = Mockito.spy(list);
        //下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
        //when(spy.get(0)).thenReturn(3);

        //使用doReturn-when可以避免when-thenReturn调用真实对象api
        Mockito.doReturn(999).when(spy).get(999);
        //预设size()期望值
        Mockito.when(spy.size()).thenReturn(100);
        //调用真实对象的api
        spy.add(1);
        spy.add(2);
        Assert.assertEquals(100, spy.size());
        Assert.assertEquals(1, spy.get(0));
        Assert.assertEquals(2, spy.get(1));
        Mockito.verify(spy).add(1);
        Mockito.verify(spy).add(2);
        Assert.assertEquals(999, spy.get(999));
        spy.get(2);
    }

    /**
     * 真实的部分mock
     */
    @Test
    public void real_partial_mock() {
        //通过spy来调用真实的api
        List list = Mockito.spy(new ArrayList());
        Assert.assertEquals(0, list.size());
        A a = Mockito.mock(A.class);
        //通过thenCallRealMethod来调用真实的api
        Mockito.when(a.doSomething(Matchers.anyInt())).thenCallRealMethod();
        Assert.assertEquals(999, a.doSomething(999));
    }

    class A {
        public int doSomething(int i) {
            return i;
        }
    }

    /**
     * 重置mock
     */
    @Test
    public void reset_mock() {
        List list = Mockito.mock(List.class);
        Mockito.when(list.size()).thenReturn(10);
        list.add(1);
        Assert.assertEquals(10, list.size());
        //重置mock，清除所有的互动和预设
        Mockito.reset(list);
        Assert.assertEquals(0, list.size());
    }

    /**
     * 验证确切的调用次数
     */
    @Test
    public void verifying_number_of_invocations() {
        List list = Mockito.mock(List.class);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(3);
        //验证是否被调用一次，等效于下面的times(1)
        Mockito.verify(list).add(1);
        Mockito.verify(list, Mockito.times(1)).add(1);
        //验证是否被调用2次
        Mockito.verify(list, Mockito.times(2)).add(2);
        //验证是否被调用3次
        Mockito.verify(list, Mockito.times(3)).add(3);
        //验证是否从未被调用过
        Mockito.verify(list, Mockito.never()).add(4);
        //验证至少调用一次
        Mockito.verify(list, Mockito.atLeastOnce()).add(1);
        //验证至少调用2次
        Mockito.verify(list, Mockito.atLeast(2)).add(2);
        //验证至多调用3次
        Mockito.verify(list, Mockito.atMost(3)).add(3);
    }

    /**
     * 连续调用
     */
    @Test(expected = RuntimeException.class)
    public void consecutive_calls() {
        //模拟连续调用返回期望值，如果分开，则只有最后一个有效
        Mockito.when(mockList.get(0)).thenReturn(0);
        Mockito.when(mockList.get(0)).thenReturn(1);
        Mockito.when(mockList.get(0)).thenReturn(2);
        Mockito.when(mockList.get(1)).thenReturn(0).thenReturn(1).thenThrow(new RuntimeException());
        Assert.assertEquals(2, mockList.get(0));
        Assert.assertEquals(2, mockList.get(0));
        Assert.assertEquals(0, mockList.get(1));
        Assert.assertEquals(1, mockList.get(1));
        //第三次或更多调用都会抛出异常
        mockList.get(1);
    }

    /**
     * 验证执行顺序
     */
    @Test
    public void verification_in_order() {
        List list = Mockito.mock(List.class);
        List list2 = Mockito.mock(List.class);
        list.add(1);
        list2.add("hello");
        list.add(2);
        list2.add("world");
        //将需要排序的mock对象放入InOrder
        InOrder inOrder = Mockito.inOrder(list, list2);
        //下面的代码不能颠倒顺序，验证执行顺序
        inOrder.verify(list).add(1);
        inOrder.verify(list2).add("hello");
        inOrder.verify(list).add(2);
        inOrder.verify(list2).add("world");
    }

    /**
     * 确保模拟对象上无互动发生
     */
    @Test
    public void verify_interaction() {
        List list = Mockito.mock(List.class);
        List list2 = Mockito.mock(List.class);
        List list3 = Mockito.mock(List.class);
        list.add(1);
        Mockito.verify(list).add(1);
        Mockito.verify(list, Mockito.never()).add(2);
        //验证零互动行为
        Mockito.verifyZeroInteractions(list2, list3);
    }

    /**
     * 找出冗余的互动(即未被验证到的)
     */
    @Test(expected = NoInteractionsWanted.class)

    public void find_redundant_interaction() {
        List list = Mockito.mock(List.class);
        list.add(1);
        list.add(2);
        Mockito.verify(list, Mockito.times(2)).add(Matchers.anyInt());
        //检查是否有未被验证的互动行为，因为add(1)和add(2)都会被上面的anyInt()验证到，所以下面的代码会通过
        Mockito.verifyNoMoreInteractions(list);

        List list2 = Mockito.mock(List.class);
        list2.add(1);
        list2.add(2);
        Mockito.verify(list2).add(1);
        //检查是否有未被验证的互动行为，因为add(2)没有被验证，所以下面的代码会失败抛出异常
        Mockito.verifyNoMoreInteractions(list2);
    }
}
