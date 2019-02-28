package com.example.elasticsearch;

import com.example.elasticsearch.model.*;
import com.example.elasticsearch.model.tasks.*;
import com.example.elasticsearch.repositories.AdvisorRepository;
import com.example.elasticsearch.repositories.CaseRepository;
import com.example.elasticsearch.repositories.CitizenRepository;
import com.example.elasticsearch.services.CaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.randname.RandomNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ElasticsearchApplicationTests {

    public static final String STATE_OPEN = "offen";
    public static final String STATE_CLOSED = "geschlossen";

	@Autowired
	CaseService service;

	@Autowired
    CaseRepository caseRepository;

	@Autowired
    AdvisorRepository advisorRepository;

	@Autowired
    CitizenRepository citizenRepository;

	@Autowired
    GeoApiContext geoApiContext;

	@Autowired
    ObjectMapper objectMapper;

	private final ArrayList<Integer> taskTypes = Lists.newArrayList(
	        1, // letter
            2, //note
            2,
            2,
            2,
            2,
            3, // phone
            3,
            4, // offense
            5, // resubmission
            5,
            5,
            6,
            6 // visit
    );

	private final List<String> advisorIds = Lists.newArrayList(
            "BY9H9HVNRJ65M8UWX24FMKILGWDF8LRBS3L",
            "XCJ4G2QLC4IT6BBHIFMK3YZRNXSSSEIAZXO",
            "LQV314GGKG1GRMS3KMQ6SQ0XLQBSNZJMJON",
            "3OBT1KW20MQ3ZKJ62EMXETJOUQU1O2LURAD",
            // ab hier dann die statistische Verteilung
            "LQV314GGKG1GRMS3KMQ6SQ0XLQBSNZJMJON",
            "LQV314GGKG1GRMS3KMQ6SQ0XLQBSNZJMJON",
            "XCJ4G2QLC4IT6BBHIFMK3YZRNXSSSEIAZXO"
    );

	private final List<String> citicensIds = Lists.newArrayList(
            "RY3ELKK3PEQONCSDCVVRIXQVJVPL7Q2S2MN",
            "BMCCUVE8NMCM5FWFQAC5UKTOLEAKRC3FQ3T",
            "HSQQSIOMHSF5OEPIXACSGVGNKINKA2VIDOE",
            "HWLW1501KPAZ7JEPNZSPBJEHUSKAL4VB596",
            "BDPJVZGVRYP0W4XCHSUWLXVHVOQ2ZV1LAU4",
            "ZOYRBOYWH62CYFAOP4WTMDFEWSZSSSIPRAF",
            "9Y18YPJ4BSG547WTBOMGG2IZPHP1AWVCUFY",
            "AM0FXCO6CYDBKSXINPUAWNRCPNOOZ1W1Z0P",
            "SPENH9AYMXV7BXFJD1OLXLOPRDVPNDYPQSD",
            "GL8MNRHL8RKUQHM2XDGGDUS5PZRAISZTHLT",
            "1KILD4XLPIFQ1T5KRP7UVLUYVGW6D8HJHHQ",
            "MVRBVFDXIIDM6M9YIPEYATJBTOGVKXBXW1X",
            "BZTLWYC4BN8BWH3WDJNP6QV4AXVY1RWIPJL",
            "KJRCSMFRDHFUKSNAULHC2RKX3B7IVKLIZR9",
            "1V4YANURX1JBIIVBZZIE1GHGDPFFPWLSIFI",
            "EGBXB8TQU9RPX7UMTMJ7WFFNOSHMBWCLNFO",
            "DRVKHT1HMUQOKUNK2FRWFKEFMM5KMJNYQVP",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "EREPRNAXHMTIGLFBCELZ4NOALMBJXQRSPBI",
            "I29XF8ZGSOC6C1HVJSDZU8WNGGBMHZRTQ2O",
            // ab hier dann die statistische Verteilung
            "DRVKHT1HMUQOKUNK2FRWFKEFMM5KMJNYQVP",
            "DRVKHT1HMUQOKUNK2FRWFKEFMM5KMJNYQVP",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "0UBWAXRMVRRGKFNERNJ5CSA8BVOSJLWP0JB",
            "EREPRNAXHMTIGLFBCELZ4NOALMBJXQRSPBI",
            "I29XF8ZGSOC6C1HVJSDZU8WNGGBMHZRTQ2O",
            "I29XF8ZGSOC6C1HVJSDZU8WNGGBMHZRTQ2O",
            "I29XF8ZGSOC6C1HVJSDZU8WNGGBMHZRTQ2O"
    );

	private final Lorem lorem = LoremIpsum.getInstance();
    private final RandomNameGenerator rnd = new RandomNameGenerator(0);


//	@Test
//	public void testSave() {
//        Case one = this.service.saveOne("BBB");
//        log.info("ID --> {}", one.getId());
//        one.setDescription("Hansi Booo");
//        this.service.updateIt(one);
//    }


    @Test
    public void testCreateAdvisors() {
        Advisor advisor1 = new Advisor();
        advisor1.setFirstname("Hans");
        advisor1.setLastname("Müller");
        advisor1.setShorthandSymbol("HMÜ");
        advisor1.setId(this.advisorIds.get(0));

        this.advisorRepository.save(advisor1);
        log.info("saved 1");

        Advisor advisor2 = new Advisor();
        advisor2.setFirstname("Andrea");
        advisor2.setLastname("Schmidt");
        advisor2.setShorthandSymbol("ASC");
        advisor2.setId(this.advisorIds.get(1));

        this.advisorRepository.save(advisor2);
        log.info("saved 2");

        Advisor advisor3 = new Advisor();
        advisor3.setFirstname("Paul");
        advisor3.setLastname("Meier");
        advisor3.setShorthandSymbol("PME");
        advisor3.setId(this.advisorIds.get(2));

        this.advisorRepository.save(advisor3);
        log.info("saved 3");

        Advisor advisor4 = new Advisor();
        advisor4.setFirstname("Dorothea");
        advisor4.setLastname("Murks");
        advisor4.setShorthandSymbol("DMU");
        advisor4.setId(this.advisorIds.get(3));

        this.advisorRepository.save(advisor4);
        log.info("saved 4");
    }

    @Test
    public void testManipulateData() {
        Optional<Case> aCase = this.caseRepository.findById("SSJAUGZZ4SMSAGS4IV42AM8AE7K6DLRQB8L");
        if(aCase.isPresent()) {
            Case c = aCase.get();
            ArrayList<Task> tasks = c.getTasks();
            CloseTask closeTask = this.closeTask(new Date(), c.getAdvisor());
            // set close task
            tasks.add(closeTask);
            c.setState(STATE_CLOSED);
            // speichern
            this.caseRepository.save(c);
        }
    }

    @Test
    public void testCreateCitizens() throws Exception {
        for(int i = 0; i < 20; i++) {
            Citizen citizen = new Citizen();

            citizen.setFirstname(this.lorem.getFirstName());
            citizen.setLastname(this.lorem.getLastName());
            citizen.setId(this.citicensIds.get(i));
            citizen.setAddress(this.createAddress(this.getCitizenPoints().get(i)[0], this.getCitizenPoints().get(i)[1]));

            this.citizenRepository.save(citizen);
        }
    }

    @Test
    public void testCreateMany() throws Exception {
        int cnt = 0;
	    for(List<Double> point : this.getSomePoints()) {
            String id = RandomStringUtils.randomAlphanumeric(35).toUpperCase();

            Case aCase = new Case();
            aCase.setId(id);
            aCase.setState(STATE_OPEN);
            aCase.setTitle(this.rnd.next());
            aCase.setDescription(this.lorem.getWords(10,30));
            aCase.setAddress(this.createAddress(point.get(0), point.get(1)));

            // Eigentümer
            log.info("crunching owner...");
            String ownerId = this.citicensIds.get(ThreadLocalRandom.current().nextInt(this.citicensIds.size()));
            Optional<Citizen> optionalOwner = this.citizenRepository.findById(ownerId);
            if(optionalOwner.isPresent()) {
                Citizen owner = optionalOwner.get();

                // set citizen
                if(owner.getReferencedFrom() == null) {
                    owner.setReferencedFrom(new ArrayList<>());
                }
                owner.getReferencedFrom().add(aCase.getId());
                this.citizenRepository.save(owner);

                // Liste der Referenzen leeren
                owner.getReferencedFrom().clear();
                aCase.setOwner(owner);
            } else {
                log.warn("cannot find owner with id {}", ownerId);
            }

            // Sachbearbeiter
            log.info("crunching advisor...");
            String advisorId = this.advisorIds.get(ThreadLocalRandom.current().nextInt(this.advisorIds.size()));
            Optional<Advisor> optionalAdvisor = this.advisorRepository.findById(advisorId);
            Advisor advisor = null;
            if(optionalAdvisor.isPresent()) {
                advisor = optionalAdvisor.get();

                // set advisor
                if(advisor.getReferencedFrom() == null) {
                    advisor.setReferencedFrom(new ArrayList<>());
                }
                advisor.getReferencedFrom().add(aCase.getId());
                this.advisorRepository.save(advisor);

                // Liste der Referenzen leeren
                advisor.getReferencedFrom().clear();
                aCase.setAdvisor(advisor);
            } else {
                log.warn("cannot find advisor with id {}", advisorId);
            }

            // add a create task
//            CreateTask createTask = new CreateTask();
//            Date createDate = this.createRandomDate(this.parseDate("01.01.2017"), new Date());
//            createTask.setCreated(new DateTime().toDate());
//            createTask.setComment("Fall wurde eröffnet.");

            // add task list
            int x = ThreadLocalRandom.current().nextInt(1, 20);
            List<Date> dates = this.createDateLine("01.06.2017", x, 5, 100);
            List<Task> tasks = this.randomTasks(dates, advisor, false);

            aCase.getTasks().addAll(tasks);

            this.caseRepository.save(aCase);

            cnt++;
            log.info("counter: {}", cnt);
        }

    }

    @Test
    public void testCreateTaskList() {
        Advisor advisor3 = new Advisor();
        advisor3.setFirstname("Paul");
        advisor3.setLastname("Meier");
        advisor3.setShorthandSymbol("PME");
        advisor3.setId(this.advisorIds.get(2));

        List<Date> dateList =  this.createDateLine("13.10.2017", 15, 5, 60);
        List<Task> tasks = this.randomTasks(dateList, advisor3, false);
        tasks.forEach(t -> {
            log.info("Task: " + t.toString());
        });
    }

    private List<Task> randomTasks(List<Date> dates, Advisor advisor, Boolean finished) {
        List<Task> tasks = new ArrayList<>();
        // immer mit einem create task beginnen
        tasks.add(this.createTask(dates.get(0), advisor));

        int size = dates.size();
        if(finished) {
            size--;
        }

        for(int i = 1; i < size; i++) {
            int type = this.taskTypes.get(ThreadLocalRandom.current().nextInt(0, this.taskTypes.size()));
            switch (type) {
                case 1:
                    tasks.add(this.letterTask(dates.get(i), advisor));
                    break;
                case 2:
                    tasks.add(this.noteTask(dates.get(i), advisor));
                    break;
                case 3:
                    tasks.add(this.phoneTask(dates.get(i), advisor));
                    break;
                case 4:
                    tasks.add(this.offenseTask(dates.get(i), advisor));
                    break;
                case 5:
                    tasks.add(this.resubmissionTask(dates.get(i), advisor));
                    break;
                case 6:
                    tasks.add(this.visitTask(dates.get(i), advisor));
                    break;
            }
        }

        if(finished) {
            tasks.add(this.closeTask(dates.get(size), advisor));
        }

        return tasks;
    }

    private Date createRandomDate(Date start, Date end) {
        long random = ThreadLocalRandom.current().nextLong(start.getTime(), end.getTime());
        return new Date(random);
    }

    private Date parseDate(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date inputDate = dateFormat.parse(date);
            return inputDate;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Date parseDateTime(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date inputDate = dateFormat.parse(date);
            return inputDate;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getDateTimeAsString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return  dateFormat.format(date);
    }

    private String getDateAsString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return  dateFormat.format(date);
    }

    private List<Date> createDateLine(String start, int cnt, int min, int max) {
        List<Date> dates = new ArrayList<>();
        Date input = this.parseDate(start);
        LocalDate startDate =  input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for(int i = 0; i < cnt; i++) {
            int plus = ThreadLocalRandom.current().nextInt(min, max);
            LocalDate date = startDate.plusDays(plus);
            dates.add(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            startDate = date;
        }

        return dates;
    }

    private CreateTask createTask(Date date, Advisor advisor) {
        CreateTask task = new CreateTask();
        task.setComment("Fall wurde eröffnet.");
        this.fillIt(task, advisor, date);
        return task;
    }

    private CloseTask closeTask(Date date, Advisor advisor) {
        CloseTask task = new CloseTask();
        task.setComment("Fall wurde eröffnet.");
        this.fillIt(task, advisor, date);
        return task;
    }

    private LetterTask letterTask(Date date, Advisor advisor) {
        LetterTask task = new LetterTask();
        task.setComment(this.lorem.getWords(20, 40));
        task.setUrlToCopy(this.lorem.getUrl());
        this.fillIt(task, advisor, date);
        return task;
    }

    private NoteTask noteTask(Date date, Advisor advisor) {
        NoteTask task = new NoteTask();
        task.setNote(this.lorem.getWords(5, 25));
        this.fillIt(task, advisor, date);
        return task;
    }

    private OffenseTask offenseTask(Date date, Advisor advisor) {
        OffenseTask task = new OffenseTask();
        double amount = ThreadLocalRandom.current().nextDouble(250.00, 25000.00);
        amount = Math.ceil(amount / 10) * 10;
        task.setAmount(amount);
        task.setReason(this.lorem.getWords(30, 100));
        this.fillIt(task, advisor, date);
        return task;
    }

    private PhoneTask phoneTask(Date date, Advisor advisor) {
        PhoneTask task = new PhoneTask();
        String d = this.getDateAsString(date);
        List<String> timeGap = this.getRandomTimeGap();
        task.setStart(this.parseDateTime(d + " " + timeGap.get(0)));
        task.setEnd(this.parseDateTime(d + " " + timeGap.get(1)));
        task.setNumber(this.lorem.getPhone());
        task.setComment(this.lorem.getWords(20, 40));
        this.fillIt(task, advisor, date);
        return task;
    }

    private ResubmissionTask resubmissionTask(Date date, Advisor advisor) {
        ResubmissionTask task = new ResubmissionTask();
        task.setResubmissionDate(getRandomPlusDate(date));
        task.setComment(this.lorem.getWords(20, 40));
        this.fillIt(task, advisor, date);
        return task;
    }

    private VisitTask visitTask(Date date, Advisor advisor) {
        VisitTask task = new VisitTask();
        String d = this.getDateAsString(date);
        List<String> timeGap = this.getRandomTimeGap();
        task.setStart(this.parseDateTime(d + " " + timeGap.get(0)));
        task.setEnd(this.parseDateTime(d + " " + timeGap.get(1)));
        task.setComment(this.lorem.getWords(20, 40));
        this.fillIt(task, advisor, date);
        return task;
    }

    private Date getRandomPlusDate(Date date) {
        LocalDate startDate =  date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int plus = ThreadLocalRandom.current().nextInt(10, 100);
        LocalDate plusDate = startDate.plusDays(plus);
        return Date.from(plusDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private List<String> getRandomTimeGap() {
        int hour1 = ThreadLocalRandom.current().nextInt(6, 18);
        int minute1 = ThreadLocalRandom.current().nextInt(0, 60);
        int minutes = ThreadLocalRandom.current().nextInt(1, 25);
        int minute2 = minute1 + minutes;
        int hour2 = hour1;
        if(minute2 > 59) {
            minute2 = minute2 - 59;
            hour2 = hour1 + 1;
        }
        // format time
        return  Lists.newArrayList(
            StringUtils.leftPad(String.valueOf(hour1), 2, "0") + ":" + StringUtils.leftPad(String.valueOf(minute1), 2, "0"),
            StringUtils.leftPad(String.valueOf(hour2), 2, "0") + ":" + StringUtils.leftPad(String.valueOf(minute2), 2, "0")
        );
    }

    private void fillIt(Task task, Advisor advisor, Date date) {
        task.setAdvisorId(advisor.getId());
        task.setAdvisorShorthandSymbol(advisor.getShorthandSymbol());
        task.setCreated(date);
    }

//    @Test
//    public void testSaveId() {
//        String id = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
//        log.info("ID --> {}", id);
//        Case aCase = this.service.saveSomething(id, "ZZZZ");
//        log.info("ID --> {}", aCase.getId());
//
//        // add geopoint
//        GeoPoint geoPoint = new GeoPoint(48.173243, 11.536002);
//        Case aCase1 = this.service.addGeoPoint(geoPoint, aCase);
//
//        // add some nested items
//        VisitTask visitTask = new VisitTask();
//        visitTask.setComment("My fancy Foo Task!");
//        visitTask.setFoo("yo - foo");
////        visitTask.setCreated("2019-02-05 13:53:00");
//        visitTask.setCreated(new DateTime().toDate());
//
//        LetterTask letterTask = new LetterTask();
//        letterTask.setComment("Ok - Brief gesendet");
//
//        log.info("suche per id");
//
//        ArrayList<Task> tasks = Lists.newArrayList(visitTask, letterTask);
//        aCase1.setTasks(tasks);
//        this.service.updateIt(aCase1);
//
//        Optional<Case> optionalCase = this.caseRepository.findById(id);
//        if(optionalCase.isPresent()) {
//            log.info("gefunden: {}", optionalCase.get().getTitle());
//        }
//
////        this.caseRepository.deleteById(id);
//
//    }

    private Address createAddress(Double lat, Double lng) throws Exception {
	    Address address = new Address();
	    address.setLocation(new GeoPoint(lat, lng));

        GeocodingResult[] results = GeocodingApi.reverseGeocode(this.geoApiContext, new LatLng(lat, lng)).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        AddressComponent[] addressComponents = results[0].addressComponents;
        String json = gson.toJson(results[0].addressComponents);

        for (AddressComponent a : addressComponents) {

            AddressPart part = this.objectMapper.readValue(gson.toJson(a), AddressPart.class);

            if(part.getTypes().contains("STREET_NUMBER")) {
                address.setStreetNumber(a.longName);
            }

            if(part.getTypes().contains("ROUTE")) {
                address.setStreet(a.longName);
            }

            if(part.getTypes().contains("SUBLOCALITY")) {
                address.setSublocality(a.longName);
            }

            if(part.getTypes().contains("LOCALITY")) {
                address.setCity(a.longName);
            }

            if(part.getTypes().contains("COUNTRY")) {
                address.setCountry(a.longName);
            }

            if(part.getTypes().contains("POSTAL_CODE")) {
                address.setPostalCode(a.longName);
            }
        }
        log.info("Address for {} / {} -> {}", lat, lng, address.toString());

	    return address;
    }

    private Citizen createOwner(String id, double[] point) {
        Citizen citizen = new Citizen();

        citizen.setId(id);
        citizen.setFirstname(this.lorem.getFirstName());
        citizen.setLastname(this.lorem.getLastName());


        return citizen;
    }

    private List<List<Double>> getSomePoints() {
        return Lists.newArrayList(
                Lists.newArrayList(48.16167275, 11.61991766),
                Lists.newArrayList(48.1117484, 11.52079443),
                Lists.newArrayList(48.11515112, 11.69384272),
                Lists.newArrayList(48.18335754, 11.71988774),
                Lists.newArrayList(48.13753213, 11.5769196),
                Lists.newArrayList(48.11847265, 11.72277204),
                Lists.newArrayList(48.13009494, 11.67089579),
                Lists.newArrayList(48.11559341, 11.50432307),
                Lists.newArrayList(48.12484573, 11.5256366),
                Lists.newArrayList(48.13946925, 11.56178583),
                Lists.newArrayList(48.15694186, 11.60403523),
                Lists.newArrayList(48.15955031, 11.54534123),
                Lists.newArrayList(48.17805417, 11.72004391),
                Lists.newArrayList(48.12145131, 11.61980808),
                Lists.newArrayList(48.12119736, 11.54007131),
                Lists.newArrayList(48.1195967, 11.53081286),
                Lists.newArrayList(48.1275286, 11.55746023),
                Lists.newArrayList(48.12677812, 11.65911377),
                Lists.newArrayList(48.11042544, 11.68071145),
                Lists.newArrayList(48.15300402, 11.47945508),
                Lists.newArrayList(48.19469561, 11.56638601),
                Lists.newArrayList(48.11817576, 11.53319607),
                Lists.newArrayList(48.18832252, 11.55416933),
                Lists.newArrayList(48.1982357, 11.56344308),
                Lists.newArrayList(48.10991328, 11.59936225),
                Lists.newArrayList(48.19452414, 11.54204874),
                Lists.newArrayList(48.1530843, 11.69470858),
                Lists.newArrayList(48.13171221, 11.51290162),
                Lists.newArrayList(48.11316863, 11.48110062),
                Lists.newArrayList(48.15644632, 11.58939746),
                Lists.newArrayList(48.1721628, 11.58540927),
                Lists.newArrayList(48.13552469, 11.64215703),
                Lists.newArrayList(48.15774616, 11.4768924),
                Lists.newArrayList(48.12871459, 11.48081155),
                Lists.newArrayList(48.15369478, 11.60115691),
                Lists.newArrayList(48.11550294, 11.50448787),
                Lists.newArrayList(48.11045073, 11.50707424),
                Lists.newArrayList(48.11057009, 11.55212057),
                Lists.newArrayList(48.18612301, 11.71044694),
                Lists.newArrayList(48.18099136, 11.57530242),
                Lists.newArrayList(48.11073367, 11.63770731)
        );
    }

    private ArrayList<double[]> getCitizenPoints() {
	    return  Lists.newArrayList(
                new double[] {48.26995896, 11.03747967},
                new double[] {47.40469538, 11.60321856},
                new double[] {48.53545218, 11.53712179},
                new double[] {49.687658, 11.633467},
                new double[] {47.91400846, 11.33093673},
                new double[] {47.71992976, 12.03784538},
                new double[] {48.27612346, 12.88763869},
                new double[] {47.63991672, 11.38003155},
                new double[] {47.65268419, 12.14799397},
                new double[] {48.20643981, 12.31501059},
                new double[] {47.83648697, 11.83004227},
                new double[] {48.998968, 12.080881},
                new double[] {48.38030798, 12.18532937},
                new double[] {48.38110885, 12.00070793},
                new double[] {48.02018595, 10.7167031},
                new double[] {47.9867959, 11.27210428},
                new double[] {47.29104362, 11.85597388},
                new double[] {47.88619363, 12.32826697},
                new double[] {48.13330023, 11.09296443},
                new double[] {47.713353, 11.756455}
                );
    }

//    private List<List<Double>> getPoints() {
//        return Lists.newArrayList(
//                Lists.newArrayList(48.16167275, 11.61991766),
//                Lists.newArrayList(48.1117484, 11.52079443),
//                Lists.newArrayList(48.11515112, 11.69384272),
//                Lists.newArrayList(48.18335754, 11.71988774),
//                Lists.newArrayList(48.13753213, 11.5769196),
//                Lists.newArrayList(48.11847265, 11.72277204),
//                Lists.newArrayList(48.13009494, 11.67089579),
//                Lists.newArrayList(48.11559341, 11.50432307),
//                Lists.newArrayList(48.12484573, 11.5256366),
//                Lists.newArrayList(48.13946925, 11.56178583),
//                Lists.newArrayList(48.15694186, 11.60403523),
//                Lists.newArrayList(48.15955031, 11.54534123),
//                Lists.newArrayList(48.17805417, 11.72004391),
//                Lists.newArrayList(48.12145131, 11.61980808),
//                Lists.newArrayList(48.12119736, 11.54007131),
//                Lists.newArrayList(48.1195967, 11.53081286),
//                Lists.newArrayList(48.1275286, 11.55746023),
//                Lists.newArrayList(48.12677812, 11.65911377),
//                Lists.newArrayList(48.11042544, 11.68071145),
//                Lists.newArrayList(48.15300402, 11.47945508),
//                Lists.newArrayList(48.19469561, 11.56638601),
//                Lists.newArrayList(48.11817576, 11.53319607),
//                Lists.newArrayList(48.18832252, 11.55416933),
//                Lists.newArrayList(48.1982357, 11.56344308),
//                Lists.newArrayList(48.10991328, 11.59936225),
//                Lists.newArrayList(48.19452414, 11.54204874),
//                Lists.newArrayList(48.1530843, 11.69470858),
//                Lists.newArrayList(48.13171221, 11.51290162),
//                Lists.newArrayList(48.11316863, 11.48110062),
//                Lists.newArrayList(48.15644632, 11.58939746),
//                Lists.newArrayList(48.1721628, 11.58540927),
//                Lists.newArrayList(48.13552469, 11.64215703),
//                Lists.newArrayList(48.15774616, 11.4768924),
//                Lists.newArrayList(48.12871459, 11.48081155),
//                Lists.newArrayList(48.15369478, 11.60115691),
//                Lists.newArrayList(48.11550294, 11.50448787),
//                Lists.newArrayList(48.11045073, 11.50707424),
//                Lists.newArrayList(48.11057009, 11.55212057),
//                Lists.newArrayList(48.18612301, 11.71044694),
//                Lists.newArrayList(48.18099136, 11.57530242),
//                Lists.newArrayList(48.11073367, 11.63770731),
//                Lists.newArrayList(48.16676008, 11.72226331),
//                Lists.newArrayList(48.11735536, 11.60977576),
//                Lists.newArrayList(48.11905302, 11.50689735),
//                Lists.newArrayList(48.17780879, 11.68662576),
//                Lists.newArrayList(48.19876061, 11.72348103),
//                Lists.newArrayList(48.12807978, 11.49246716),
//                Lists.newArrayList(48.11846646, 11.46422754),
//                Lists.newArrayList(48.12972937, 11.71444903),
//                Lists.newArrayList(48.15297249, 11.50004715),
//                Lists.newArrayList(48.17818276, 11.54822413),
//                Lists.newArrayList(48.19722328, 11.58830017),
//                Lists.newArrayList(48.11383685, 11.72162789),
//                Lists.newArrayList(48.18045643, 11.64765473),
//                Lists.newArrayList(48.1683616, 11.57225933),
//                Lists.newArrayList(48.15683141, 11.69553338),
//                Lists.newArrayList(48.19626135, 11.51139479),
//                Lists.newArrayList(48.14249658, 11.53520375),
//                Lists.newArrayList(48.12667066, 11.46249336),
//                Lists.newArrayList(48.17300587, 11.63447403),
//                Lists.newArrayList(48.14583845, 11.56164062),
//                Lists.newArrayList(48.16558798, 11.50059568),
//                Lists.newArrayList(48.12662844, 11.55204426),
//                Lists.newArrayList(48.16265161, 11.57846317),
//                Lists.newArrayList(48.10863067, 11.65115275),
//                Lists.newArrayList(48.15908305, 11.5834132),
//                Lists.newArrayList(48.11149127, 11.47485378),
//                Lists.newArrayList(48.17039365, 11.48144428),
//                Lists.newArrayList(48.17787268, 11.5012375),
//                Lists.newArrayList(48.1106267, 11.70860114),
//                Lists.newArrayList(48.10710544, 11.56998248),
//                Lists.newArrayList(48.18108255, 11.51283725),
//                Lists.newArrayList(48.13407688, 11.58198091),
//                Lists.newArrayList(48.14730113, 11.72227605),
//                Lists.newArrayList(48.19214369, 11.52982844),
//                Lists.newArrayList(48.1876285, 11.6291796),
//                Lists.newArrayList(48.16839183, 11.56031899),
//                Lists.newArrayList(48.1431213, 11.64878922),
//                Lists.newArrayList(48.1212408, 11.70240905),
//                Lists.newArrayList(48.14222635, 11.56654795),
//                Lists.newArrayList(48.16861345, 11.56602262),
//                Lists.newArrayList(48.13336221, 11.71109465),
//                Lists.newArrayList(48.15404873, 11.66308714),
//                Lists.newArrayList(48.18971446, 11.65803783),
//                Lists.newArrayList(48.15955874, 11.49075224),
//                Lists.newArrayList(48.19746614, 11.6857363),
//                Lists.newArrayList(48.13366506, 11.7220203),
//                Lists.newArrayList(48.13654879, 11.51813371),
//                Lists.newArrayList(48.11466458, 11.54008968),
//                Lists.newArrayList(48.17041558, 11.51080696),
//                Lists.newArrayList(48.13900279, 11.49008717),
//                Lists.newArrayList(48.19700968, 11.55309798),
//                Lists.newArrayList(48.14378461, 11.64739657),
//                Lists.newArrayList(48.19873336, 11.67924623),
//                Lists.newArrayList(48.18417703, 11.65507107),
//                Lists.newArrayList(48.12263385, 11.48938711),
//                Lists.newArrayList(48.11547564, 11.52641714),
//                Lists.newArrayList(48.12805426, 11.67008823),
//                Lists.newArrayList(48.12669043, 11.68797605),
//                Lists.newArrayList(48.11344686, 11.70903585),
//                Lists.newArrayList(48.14761886, 11.53823149),
//                Lists.newArrayList(48.16211057, 11.54697022),
//                Lists.newArrayList(48.16069691, 11.6312742),
//                Lists.newArrayList(48.1867452, 11.71725341),
//                Lists.newArrayList(48.19557103, 11.5521706),
//                Lists.newArrayList(48.12521899, 11.66951004),
//                Lists.newArrayList(48.19384689, 11.54119009),
//                Lists.newArrayList(48.13423686, 11.55571021),
//                Lists.newArrayList(48.17314336, 11.71563457),
//                Lists.newArrayList(48.13819455, 11.52186198),
//                Lists.newArrayList(48.19575352, 11.50033374),
//                Lists.newArrayList(48.13154704, 11.69310033),
//                Lists.newArrayList(48.11170155, 11.65760285),
//                Lists.newArrayList(48.11333342, 11.6593111),
//                Lists.newArrayList(48.11298289, 11.52161274),
//                Lists.newArrayList(48.16563826, 11.69523781),
//                Lists.newArrayList(48.19199964, 11.7010145),
//                Lists.newArrayList(48.19951685, 11.51807485),
//                Lists.newArrayList(48.14590834, 11.46119967),
//                Lists.newArrayList(48.139053, 11.6333387),
//                Lists.newArrayList(48.19195815, 11.56955836),
//                Lists.newArrayList(48.13070502, 11.65474313),
//                Lists.newArrayList(48.14433019, 11.48911729),
//                Lists.newArrayList(48.11366157, 11.60478032),
//                Lists.newArrayList(48.16064975, 11.62765645),
//                Lists.newArrayList(48.16080318, 11.70609728),
//                Lists.newArrayList(48.15700167, 11.66263071),
//                Lists.newArrayList(48.16521768, 11.50039657),
//                Lists.newArrayList(48.14075726, 11.62839816),
//                Lists.newArrayList(48.17515404, 11.53812142),
//                Lists.newArrayList(48.14699223, 11.47896092),
//                Lists.newArrayList(48.16357561, 11.7151088),
//                Lists.newArrayList(48.16691445, 11.67939972),
//                Lists.newArrayList(48.14963039, 11.60269798),
//                Lists.newArrayList(48.15602, 11.5806546),
//                Lists.newArrayList(48.18078384, 11.66328785),
//                Lists.newArrayList(48.18840157, 11.58275264),
//                Lists.newArrayList(48.12528151, 11.6769131),
//                Lists.newArrayList(48.16159866, 11.64860329),
//                Lists.newArrayList(48.12220853, 11.55876662),
//                Lists.newArrayList(48.1515233, 11.72044675),
//                Lists.newArrayList(48.11385898, 11.50057308),
//                Lists.newArrayList(48.14591464, 11.61799356),
//                Lists.newArrayList(48.15170524, 11.59390494),
//                Lists.newArrayList(48.17903203, 11.59833017),
//                Lists.newArrayList(48.11430226, 11.48206517),
//                Lists.newArrayList(48.18430426, 11.70361352),
//                Lists.newArrayList(48.16044034, 11.52634486),
//                Lists.newArrayList(48.17297205, 11.61763153),
//                Lists.newArrayList(48.11773569, 11.62929321),
//                Lists.newArrayList(48.18166663, 11.55665842),
//                Lists.newArrayList(48.15405035, 11.65520651),
//                Lists.newArrayList(48.174291, 11.71989072),
//                Lists.newArrayList(48.16169507, 11.46020177),
//                Lists.newArrayList(48.13600029, 11.65577305),
//                Lists.newArrayList(48.16771041, 11.54230881),
//                Lists.newArrayList(48.13191279, 11.54750973),
//                Lists.newArrayList(48.15075566, 11.67853303),
//                Lists.newArrayList(48.19034864, 11.72061297),
//                Lists.newArrayList(48.17580538, 11.50166073),
//                Lists.newArrayList(48.10625671, 11.67104964),
//                Lists.newArrayList(48.19756642, 11.62048667),
//                Lists.newArrayList(48.16194396, 11.56673933),
//                Lists.newArrayList(48.11086695, 11.59020617),
//                Lists.newArrayList(48.16582877, 11.60081401),
//                Lists.newArrayList(48.13735407, 11.6215239),
//                Lists.newArrayList(48.18228399, 11.5016913),
//                Lists.newArrayList(48.1452229, 11.49163281),
//                Lists.newArrayList(48.1527066, 11.66431627),
//                Lists.newArrayList(48.15659237, 11.7120269),
//                Lists.newArrayList(48.121406, 11.50917708),
//                Lists.newArrayList(48.13333415, 11.56464928),
//                Lists.newArrayList(48.11488498, 11.67905475),
//                Lists.newArrayList(48.13433668, 11.58094598),
//                Lists.newArrayList(48.1652742, 11.49224684),
//                Lists.newArrayList(48.17488513, 11.68621243),
//                Lists.newArrayList(48.15774652, 11.60510254),
//                Lists.newArrayList(48.16652796, 11.66676022),
//                Lists.newArrayList(48.19765699, 11.55369756),
//                Lists.newArrayList(48.18924183, 11.66679784),
//                Lists.newArrayList(48.19599647, 11.57558225),
//                Lists.newArrayList(48.1223148, 11.46231435),
//                Lists.newArrayList(48.18542236, 11.48543217),
//                Lists.newArrayList(48.162925, 11.59960183),
//                Lists.newArrayList(48.13543743, 11.56901595),
//                Lists.newArrayList(48.12902566, 11.71915774),
//                Lists.newArrayList(48.10706155, 11.50731276),
//                Lists.newArrayList(48.17231471, 11.66061198),
//                Lists.newArrayList(48.11562014, 11.5751626),
//                Lists.newArrayList(48.15686063, 11.46541251),
//                Lists.newArrayList(48.15536019, 11.58092434),
//                Lists.newArrayList(48.16764687, 11.52373074),
//                Lists.newArrayList(48.12661733, 11.51531789),
//                Lists.newArrayList(48.16905613, 11.46562603),
//                Lists.newArrayList(48.16872766, 11.67374068),
//                Lists.newArrayList(48.1790594, 11.51479282),
//                Lists.newArrayList(48.1110216, 11.68971194),
//                Lists.newArrayList(48.19260871, 11.57346896),
//                Lists.newArrayList(48.16587486, 11.48923202),
//                Lists.newArrayList(48.14254315, 11.69677726),
//                Lists.newArrayList(48.19302533, 11.60263219),
//                Lists.newArrayList(48.13551038, 11.52165442),
//                Lists.newArrayList(48.11992553, 11.50345851),
//                Lists.newArrayList(48.125813, 11.47660463),
//                Lists.newArrayList(48.14863353, 11.53969845),
//                Lists.newArrayList(48.14949577, 11.65219162),
//                Lists.newArrayList(48.10738549, 11.49468212),
//                Lists.newArrayList(48.19933684, 11.48962932),
//                Lists.newArrayList(48.1788605, 11.62853277),
//                Lists.newArrayList(48.15509751, 11.55657843),
//                Lists.newArrayList(48.11284044, 11.47662359),
//                Lists.newArrayList(48.12298077, 11.52649135),
//                Lists.newArrayList(48.15334495, 11.53638202),
//                Lists.newArrayList(48.12742593, 11.64109297),
//                Lists.newArrayList(48.12375574, 11.68569314),
//                Lists.newArrayList(48.19116794, 11.50538753),
//                Lists.newArrayList(48.19197329, 11.67223052),
//                Lists.newArrayList(48.12550053, 11.70650912),
//                Lists.newArrayList(48.19394131, 11.52686665),
//                Lists.newArrayList(48.13727951, 11.71081948),
//                Lists.newArrayList(48.15913795, 11.5110862),
//                Lists.newArrayList(48.12661497, 11.56653139),
//                Lists.newArrayList(48.18190594, 11.4973867),
//                Lists.newArrayList(48.17401475, 11.48482689),
//                Lists.newArrayList(48.10887783, 11.68120739),
//                Lists.newArrayList(48.13950905, 11.48089907),
//                Lists.newArrayList(48.12662453, 11.64293225),
//                Lists.newArrayList(48.12986588, 11.63718387),
//                Lists.newArrayList(48.12717707, 11.53486423),
//                Lists.newArrayList(48.14327652, 11.53067934),
//                Lists.newArrayList(48.16103114, 11.55796752),
//                Lists.newArrayList(48.13366373, 11.69072539),
//                Lists.newArrayList(48.14139455, 11.52039038),
//                Lists.newArrayList(48.1372229, 11.60358852),
//                Lists.newArrayList(48.19730884, 11.64082724),
//                Lists.newArrayList(48.16190374, 11.47306778),
//                Lists.newArrayList(48.19796354, 11.67827928),
//                Lists.newArrayList(48.13032111, 11.47880936),
//                Lists.newArrayList(48.14659644, 11.6382709),
//                Lists.newArrayList(48.15838919, 11.67838561),
//                Lists.newArrayList(48.11440235, 11.64177633),
//                Lists.newArrayList(48.13825125, 11.64890346),
//                Lists.newArrayList(48.17370466, 11.57602951),
//                Lists.newArrayList(48.18240809, 11.70895434),
//                Lists.newArrayList(48.14568514, 11.61642961),
//                Lists.newArrayList(48.1243331, 11.54331175),
//                Lists.newArrayList(48.16068118, 11.54250242),
//                Lists.newArrayList(48.14128932, 11.68332604),
//                Lists.newArrayList(48.18000075, 11.66874224),
//                Lists.newArrayList(48.18366574, 11.55078744),
//                Lists.newArrayList(48.16149043, 11.64564),
//                Lists.newArrayList(48.11003258, 11.62010581),
//                Lists.newArrayList(48.19950437, 11.68097492),
//                Lists.newArrayList(48.14628931, 11.49333191),
//                Lists.newArrayList(48.17058832, 11.48332181),
//                Lists.newArrayList(48.14603298, 11.5459677),
//                Lists.newArrayList(48.16226667, 11.71218513),
//                Lists.newArrayList(48.13218887, 11.64104049),
//                Lists.newArrayList(48.18337581, 11.63683713),
//                Lists.newArrayList(48.18903187, 11.598918),
//                Lists.newArrayList(48.15497167, 11.54507034),
//                Lists.newArrayList(48.18297151, 11.66670344),
//                Lists.newArrayList(48.16767028, 11.55671005),
//                Lists.newArrayList(48.19320409, 11.58708446),
//                Lists.newArrayList(48.19552202, 11.47973276),
//                Lists.newArrayList(48.18240186, 11.49546377),
//                Lists.newArrayList(48.13351106, 11.60852262),
//                Lists.newArrayList(48.11399474, 11.4844297),
//                Lists.newArrayList(48.16978071, 11.64016291),
//                Lists.newArrayList(48.16987526, 11.72110164),
//                Lists.newArrayList(48.11791003, 11.55689425),
//                Lists.newArrayList(48.19918559, 11.62520718),
//                Lists.newArrayList(48.18080361, 11.54821131),
//                Lists.newArrayList(48.19301453, 11.66488967),
//                Lists.newArrayList(48.11917111, 11.54997495),
//                Lists.newArrayList(48.17960316, 11.71351745),
//                Lists.newArrayList(48.15875965, 11.54847018),
//                Lists.newArrayList(48.14483611, 11.5203244),
//                Lists.newArrayList(48.14471465, 11.47743418),
//                Lists.newArrayList(48.13738771, 11.571405),
//                Lists.newArrayList(48.18115076, 11.53473391),
//                Lists.newArrayList(48.13500358, 11.71408532),
//                Lists.newArrayList(48.16725944, 11.67440517),
//                Lists.newArrayList(48.16300837, 11.53829385),
//                Lists.newArrayList(48.17391911, 11.70396301),
//                Lists.newArrayList(48.19184985, 11.55947649),
//                Lists.newArrayList(48.16353794, 11.52782759),
//                Lists.newArrayList(48.14751418, 11.66370569),
//                Lists.newArrayList(48.15403128, 11.47274672),
//                Lists.newArrayList(48.13493898, 11.49727188),
//                Lists.newArrayList(48.16324306, 11.66781412),
//                Lists.newArrayList(48.11172678, 11.48396505),
//                Lists.newArrayList(48.14817914, 11.52975082),
//                Lists.newArrayList(48.16460962, 11.71130778),
//                Lists.newArrayList(48.1341679, 11.64795915),
//                Lists.newArrayList(48.17529985, 11.65428354),
//                Lists.newArrayList(48.10684785, 11.48971766),
//                Lists.newArrayList(48.1926372, 11.47229317),
//                Lists.newArrayList(48.12078712, 11.53041819),
//                Lists.newArrayList(48.18443815, 11.71244169),
//                Lists.newArrayList(48.1065693, 11.61232507),
//                Lists.newArrayList(48.15824784, 11.70019886),
//                Lists.newArrayList(48.16786212, 11.52044343),
//                Lists.newArrayList(48.16354929, 11.653538),
//                Lists.newArrayList(48.19552655, 11.69713049),
//                Lists.newArrayList(48.17591805, 11.65693441),
//                Lists.newArrayList(48.12668673, 11.55659058),
//                Lists.newArrayList(48.19841501, 11.51543521),
//                Lists.newArrayList(48.15367652, 11.71553361),
//                Lists.newArrayList(48.11681329, 11.65754707),
//                Lists.newArrayList(48.19920548, 11.48606848),
//                Lists.newArrayList(48.17953505, 11.63077526),
//                Lists.newArrayList(48.17536151, 11.53756791),
//                Lists.newArrayList(48.11008416, 11.52318328),
//                Lists.newArrayList(48.13479996, 11.48180663),
//                Lists.newArrayList(48.18090353, 11.47685493),
//                Lists.newArrayList(48.11352422, 11.63792826),
//                Lists.newArrayList(48.16250548, 11.67431196),
//                Lists.newArrayList(48.11168331, 11.51050685),
//                Lists.newArrayList(48.17580945, 11.67915011),
//                Lists.newArrayList(48.17406504, 11.57539028),
//                Lists.newArrayList(48.13436515, 11.64483156),
//                Lists.newArrayList(48.19471238, 11.54983449),
//                Lists.newArrayList(48.11449495, 11.47146774),
//                Lists.newArrayList(48.16493962, 11.60530093),
//                Lists.newArrayList(48.18460848, 11.49365202),
//                Lists.newArrayList(48.16131042, 11.47275092),
//                Lists.newArrayList(48.13779791, 11.64849742),
//                Lists.newArrayList(48.17144342, 11.65216162),
//                Lists.newArrayList(48.15373445, 11.6785832),
//                Lists.newArrayList(48.10816884, 11.63198448),
//                Lists.newArrayList(48.13889387, 11.52412924),
//                Lists.newArrayList(48.11031573, 11.65826374),
//                Lists.newArrayList(48.14218611, 11.53836958),
//                Lists.newArrayList(48.11436474, 11.49625182),
//                Lists.newArrayList(48.15824233, 11.64912093),
//                Lists.newArrayList(48.11794193, 11.69086027),
//                Lists.newArrayList(48.17643718, 11.65425298),
//                Lists.newArrayList(48.19909558, 11.575719),
//                Lists.newArrayList(48.15396553, 11.59108761),
//                Lists.newArrayList(48.19433928, 11.6995375),
//                Lists.newArrayList(48.16456277, 11.69110522),
//                Lists.newArrayList(48.11909334, 11.68646068),
//                Lists.newArrayList(48.12676068, 11.64292218),
//                Lists.newArrayList(48.10669732, 11.50425811),
//                Lists.newArrayList(48.18880512, 11.57581181),
//                Lists.newArrayList(48.18135278, 11.66074824),
//                Lists.newArrayList(48.10761554, 11.69804485),
//                Lists.newArrayList(48.16000587, 11.65585612),
//                Lists.newArrayList(48.11532182, 11.51651861),
//                Lists.newArrayList(48.10701636, 11.51977886),
//                Lists.newArrayList(48.1183156, 11.50727763),
//                Lists.newArrayList(48.15828021, 11.68447088),
//                Lists.newArrayList(48.16332197, 11.68399607),
//                Lists.newArrayList(48.16446937, 11.6206992),
//                Lists.newArrayList(48.19728733, 11.65323113),
//                Lists.newArrayList(48.12881271, 11.55987081),
//                Lists.newArrayList(48.16238212, 11.64289577),
//                Lists.newArrayList(48.17578728, 11.70438558),
//                Lists.newArrayList(48.15898826, 11.68296583),
//                Lists.newArrayList(48.17482182, 11.59748158),
//                Lists.newArrayList(48.1651939, 11.46948386),
//                Lists.newArrayList(48.1855464, 11.5693938),
//                Lists.newArrayList(48.11902581, 11.59470127),
//                Lists.newArrayList(48.12992079, 11.46778648),
//                Lists.newArrayList(48.19930628, 11.69193516),
//                Lists.newArrayList(48.1825833, 11.47514424),
//                Lists.newArrayList(48.19834728, 11.62233678),
//                Lists.newArrayList(48.11316424, 11.65486627),
//                Lists.newArrayList(48.19437036, 11.52335261),
//                Lists.newArrayList(48.16575697, 11.4904047),
//                Lists.newArrayList(48.16483392, 11.53656485),
//                Lists.newArrayList(48.1914055, 11.65951506),
//                Lists.newArrayList(48.12666422, 11.57670478),
//                Lists.newArrayList(48.16548318, 11.67089888),
//                Lists.newArrayList(48.12560488, 11.57848748),
//                Lists.newArrayList(48.1284948, 11.57474857),
//                Lists.newArrayList(48.19886985, 11.47563859),
//                Lists.newArrayList(48.15948605, 11.64094521),
//                Lists.newArrayList(48.18861035, 11.62636017),
//                Lists.newArrayList(48.13711459, 11.52573069),
//                Lists.newArrayList(48.15022251, 11.67626973),
//                Lists.newArrayList(48.17792564, 11.46524096),
//                Lists.newArrayList(48.13168353, 11.5513262),
//                Lists.newArrayList(48.15793009, 11.54940681),
//                Lists.newArrayList(48.11862185, 11.642834),
//                Lists.newArrayList(48.15760298, 11.63094442),
//                Lists.newArrayList(48.18531032, 11.65513649),
//                Lists.newArrayList(48.12520508, 11.49214532),
//                Lists.newArrayList(48.15219904, 11.59108305),
//                Lists.newArrayList(48.1552894, 11.71969373),
//                Lists.newArrayList(48.19577506, 11.54193967),
//                Lists.newArrayList(48.17784415, 11.47286479),
//                Lists.newArrayList(48.14117954, 11.55561337),
//                Lists.newArrayList(48.17194931, 11.65332538),
//                Lists.newArrayList(48.12160994, 11.55757385),
//                Lists.newArrayList(48.15921391, 11.47750267),
//                Lists.newArrayList(48.11425692, 11.59923891),
//                Lists.newArrayList(48.19266539, 11.54974805),
//                Lists.newArrayList(48.13078794, 11.49522544),
//                Lists.newArrayList(48.15150186, 11.55361863),
//                Lists.newArrayList(48.15192587, 11.71141367),
//                Lists.newArrayList(48.15569818, 11.51679867),
//                Lists.newArrayList(48.14835656, 11.50017753),
//                Lists.newArrayList(48.19180362, 11.68609073),
//                Lists.newArrayList(48.19983425, 11.69504459),
//                Lists.newArrayList(48.18048529, 11.48114686),
//                Lists.newArrayList(48.17262744, 11.69635721),
//                Lists.newArrayList(48.18201885, 11.56876599),
//                Lists.newArrayList(48.14071399, 11.71844094),
//                Lists.newArrayList(48.14921453, 11.5629818),
//                Lists.newArrayList(48.13894426, 11.64371928),
//                Lists.newArrayList(48.19618391, 11.71616496),
//                Lists.newArrayList(48.16155937, 11.6771072),
//                Lists.newArrayList(48.17386116, 11.57455996),
//                Lists.newArrayList(48.18368886, 11.63183016),
//                Lists.newArrayList(48.1753256, 11.51346269),
//                Lists.newArrayList(48.12245103, 11.63303322),
//                Lists.newArrayList(48.13606655, 11.68271459),
//                Lists.newArrayList(48.15166314, 11.51568766),
//                Lists.newArrayList(48.15246045, 11.61645451),
//                Lists.newArrayList(48.17448204, 11.63530413),
//                Lists.newArrayList(48.18946374, 11.55292983),
//                Lists.newArrayList(48.12523495, 11.6739655),
//                Lists.newArrayList(48.15650553, 11.68117234),
//                Lists.newArrayList(48.17220303, 11.69005285),
//                Lists.newArrayList(48.12860874, 11.68620404),
//                Lists.newArrayList(48.10638846, 11.69264253),
//                Lists.newArrayList(48.1507838, 11.52300755),
//                Lists.newArrayList(48.14056023, 11.68457091),
//                Lists.newArrayList(48.19457613, 11.49729658),
//                Lists.newArrayList(48.11198485, 11.64111173),
//                Lists.newArrayList(48.14725552, 11.68752749),
//                Lists.newArrayList(48.12301037, 11.5670409),
//                Lists.newArrayList(48.10912929, 11.51824314),
//                Lists.newArrayList(48.12258433, 11.55094834),
//                Lists.newArrayList(48.1687112, 11.61814207),
//                Lists.newArrayList(48.13768813, 11.61012449),
//                Lists.newArrayList(48.14990551, 11.66068852),
//                Lists.newArrayList(48.16851393, 11.56617162),
//                Lists.newArrayList(48.18393404, 11.57173211),
//                Lists.newArrayList(48.1298924, 11.71723566),
//                Lists.newArrayList(48.15941, 11.59253108),
//                Lists.newArrayList(48.15027152, 11.54283322),
//                Lists.newArrayList(48.1739776, 11.65736885),
//                Lists.newArrayList(48.17400837, 11.67421015),
//                Lists.newArrayList(48.16454662, 11.65209893),
//                Lists.newArrayList(48.10913706, 11.70964311),
//                Lists.newArrayList(48.18634781, 11.55935021),
//                Lists.newArrayList(48.15214503, 11.57774698),
//                Lists.newArrayList(48.1217529, 11.48453574),
//                Lists.newArrayList(48.15710991, 11.56387044),
//                Lists.newArrayList(48.17551733, 11.64254409),
//                Lists.newArrayList(48.16888711, 11.5326866),
//                Lists.newArrayList(48.1728062, 11.52327399),
//                Lists.newArrayList(48.14294811, 11.59919677),
//                Lists.newArrayList(48.13894772, 11.5562864),
//                Lists.newArrayList(48.11606361, 11.53230147),
//                Lists.newArrayList(48.12848555, 11.54732387),
//                Lists.newArrayList(48.12451704, 11.51092108),
//                Lists.newArrayList(48.15717398, 11.65054721),
//                Lists.newArrayList(48.13778966, 11.70677333),
//                Lists.newArrayList(48.18995145, 11.66862477),
//                Lists.newArrayList(48.19409855, 11.58141312),
//                Lists.newArrayList(48.17605924, 11.70249647),
//                Lists.newArrayList(48.19632097, 11.56407065),
//                Lists.newArrayList(48.13384024, 11.52940031),
//                Lists.newArrayList(48.11621003, 11.71830382),
//                Lists.newArrayList(48.18022292, 11.64643097),
//                Lists.newArrayList(48.12785111, 11.69633267),
//                Lists.newArrayList(48.14711147, 11.6150798),
//                Lists.newArrayList(48.16944693, 11.54786826),
//                Lists.newArrayList(48.12735932, 11.55504053),
//                Lists.newArrayList(48.12728308, 11.52206446),
//                Lists.newArrayList(48.1816534, 11.56922108),
//                Lists.newArrayList(48.12571975, 11.49958965),
//                Lists.newArrayList(48.13951362, 11.63190655),
//                Lists.newArrayList(48.14514405, 11.55331825),
//                Lists.newArrayList(48.16794533, 11.67364807),
//                Lists.newArrayList(48.17803664, 11.61893274),
//                Lists.newArrayList(48.19665371, 11.65524829),
//                Lists.newArrayList(48.10787244, 11.64945337),
//                Lists.newArrayList(48.18836756, 11.64386585),
//                Lists.newArrayList(48.1284317, 11.46031865),
//                Lists.newArrayList(48.11655669, 11.66726855),
//                Lists.newArrayList(48.16647312, 11.70683652),
//                Lists.newArrayList(48.16466089, 11.50806913),
//                Lists.newArrayList(48.17878581, 11.47025318),
//                Lists.newArrayList(48.18044315, 11.49335529),
//                Lists.newArrayList(48.14124012, 11.68824432),
//                Lists.newArrayList(48.19158095, 11.59910628),
//                Lists.newArrayList(48.15327632, 11.5556434),
//                Lists.newArrayList(48.15058467, 11.67039771),
//                Lists.newArrayList(48.17854019, 11.50395727),
//                Lists.newArrayList(48.19094737, 11.58176358),
//                Lists.newArrayList(48.16742395, 11.5072333),
//                Lists.newArrayList(48.12814093, 11.47108937),
//                Lists.newArrayList(48.16889479, 11.50525261),
//                Lists.newArrayList(48.13702349, 11.66236916),
//                Lists.newArrayList(48.1616864, 11.65253931),
//                Lists.newArrayList(48.15152067, 11.48213721),
//                Lists.newArrayList(48.15391536, 11.64204744),
//                Lists.newArrayList(48.18746356, 11.47587179),
//                Lists.newArrayList(48.11428627, 11.4917033),
//                Lists.newArrayList(48.17024617, 11.7026453),
//                Lists.newArrayList(48.11110528, 11.61353384),
//                Lists.newArrayList(48.1856409, 11.6825653),
//                Lists.newArrayList(48.1761751, 11.52234771),
//                Lists.newArrayList(48.17026446, 11.5421352),
//                Lists.newArrayList(48.18692571, 11.48642126),
//                Lists.newArrayList(48.1404146, 11.5034267),
//                Lists.newArrayList(48.11743836, 11.59692311),
//                Lists.newArrayList(48.1827768, 11.57941607),
//                Lists.newArrayList(48.15893577, 11.50772767),
//                Lists.newArrayList(48.11629881, 11.5196135),
//                Lists.newArrayList(48.12464777, 11.68157087),
//                Lists.newArrayList(48.19488067, 11.49657805),
//                Lists.newArrayList(48.14701756, 11.46088225),
//                Lists.newArrayList(48.11252493, 11.69500828),
//                Lists.newArrayList(48.12886942, 11.59209587),
//                Lists.newArrayList(48.12566207, 11.53148924),
//                Lists.newArrayList(48.15329121, 11.50501191),
//                Lists.newArrayList(48.16407981, 11.64456809),
//                Lists.newArrayList(48.13557705, 11.49847138),
//                Lists.newArrayList(48.1547861, 11.59646213),
//                Lists.newArrayList(48.14070953, 11.64986448),
//                Lists.newArrayList(48.14307352, 11.58817935),
//                Lists.newArrayList(48.1692435, 11.61204457),
//                Lists.newArrayList(48.19041292, 11.6284921),
//                Lists.newArrayList(48.18938383, 11.63548492),
//                Lists.newArrayList(48.1393409, 11.67428312),
//                Lists.newArrayList(48.18873238, 11.6639034),
//                Lists.newArrayList(48.17405083, 11.4925749),
//                Lists.newArrayList(48.18103445, 11.64913667),
//                Lists.newArrayList(48.19782927, 11.46502007),
//                Lists.newArrayList(48.18701535, 11.55761485),
//                Lists.newArrayList(48.18771699, 11.47407298),
//                Lists.newArrayList(48.11791182, 11.53054285),
//                Lists.newArrayList(48.16062985, 11.63040001),
//                Lists.newArrayList(48.19228167, 11.53063013),
//                Lists.newArrayList(48.11440381, 11.50019586),
//                Lists.newArrayList(48.19796898, 11.49488791),
//                Lists.newArrayList(48.12881851, 11.5020945),
//                Lists.newArrayList(48.19648901, 11.58080534),
//                Lists.newArrayList(48.15904904, 11.59919097),
//                Lists.newArrayList(48.14766889, 11.63876145),
//                Lists.newArrayList(48.12974776, 11.66453772),
//                Lists.newArrayList(48.14664494, 11.5063175),
//                Lists.newArrayList(48.18466572, 11.63610539),
//                Lists.newArrayList(48.12337131, 11.64492626),
//                Lists.newArrayList(48.1530007, 11.49330993),
//                Lists.newArrayList(48.16744875, 11.69642451),
//                Lists.newArrayList(48.14387895, 11.69935522),
//                Lists.newArrayList(48.12193478, 11.59775052),
//                Lists.newArrayList(48.16699725, 11.57448876),
//                Lists.newArrayList(48.12620507, 11.67031235),
//                Lists.newArrayList(48.15840185, 11.6964144),
//                Lists.newArrayList(48.12245038, 11.71766802),
//                Lists.newArrayList(48.14497639, 11.57107293),
//                Lists.newArrayList(48.11546061, 11.71122346),
//                Lists.newArrayList(48.14789268, 11.68553769),
//                Lists.newArrayList(48.18872514, 11.55567617),
//                Lists.newArrayList(48.15281963, 11.6747973),
//                Lists.newArrayList(48.14688334, 11.71232509),
//                Lists.newArrayList(48.19403547, 11.55136256),
//                Lists.newArrayList(48.12961947, 11.58161303),
//                Lists.newArrayList(48.17763888, 11.57372814),
//                Lists.newArrayList(48.12070663, 11.65498877),
//                Lists.newArrayList(48.12655036, 11.50997756),
//                Lists.newArrayList(48.16707319, 11.53962734),
//                Lists.newArrayList(48.1099901, 11.59184511),
//                Lists.newArrayList(48.18804475, 11.54789851),
//                Lists.newArrayList(48.13351578, 11.47381247),
//                Lists.newArrayList(48.1862074, 11.58561834),
//                Lists.newArrayList(48.18923946, 11.47778447),
//                Lists.newArrayList(48.13892978, 11.60272152),
//                Lists.newArrayList(48.14061884, 11.63493211),
//                Lists.newArrayList(48.1847801, 11.51154356),
//                Lists.newArrayList(48.17191043, 11.52444318),
//                Lists.newArrayList(48.15933436, 11.681373),
//                Lists.newArrayList(48.14297574, 11.66316833),
//                Lists.newArrayList(48.1282183, 11.61584022),
//                Lists.newArrayList(48.11857517, 11.52143793),
//                Lists.newArrayList(48.13259383, 11.47769309),
//                Lists.newArrayList(48.18096128, 11.66887992),
//                Lists.newArrayList(48.19178176, 11.66260035),
//                Lists.newArrayList(48.18253961, 11.630987),
//                Lists.newArrayList(48.18235236, 11.60980155),
//                Lists.newArrayList(48.13759761, 11.54646989),
//                Lists.newArrayList(48.1136044, 11.54530807),
//                Lists.newArrayList(48.11341532, 11.45986918),
//                Lists.newArrayList(48.13476551, 11.48910864),
//                Lists.newArrayList(48.11559709, 11.54353968),
//                Lists.newArrayList(48.124428, 11.46808611),
//                Lists.newArrayList(48.16337106, 11.46315114),
//                Lists.newArrayList(48.19562988, 11.57987069),
//                Lists.newArrayList(48.16089419, 11.54902054),
//                Lists.newArrayList(48.15278446, 11.63637645),
//                Lists.newArrayList(48.18582162, 11.66369178),
//                Lists.newArrayList(48.19264079, 11.63788305),
//                Lists.newArrayList(48.17843807, 11.52630016),
//                Lists.newArrayList(48.14481067, 11.50308289),
//                Lists.newArrayList(48.18810887, 11.70255976),
//                Lists.newArrayList(48.10737809, 11.58771997),
//                Lists.newArrayList(48.13616562, 11.6313688),
//                Lists.newArrayList(48.18006825, 11.71957906),
//                Lists.newArrayList(48.10652892, 11.62368446),
//                Lists.newArrayList(48.1480532, 11.62036411),
//                Lists.newArrayList(48.13825459, 11.62912152),
//                Lists.newArrayList(48.14739582, 11.49260373),
//                Lists.newArrayList(48.19967934, 11.59303782),
//                Lists.newArrayList(48.1229306, 11.5385839),
//                Lists.newArrayList(48.14267954, 11.49881023),
//                Lists.newArrayList(48.19470417, 11.46736714),
//                Lists.newArrayList(48.12307299, 11.62833299),
//                Lists.newArrayList(48.11017042, 11.62266036),
//                Lists.newArrayList(48.13180325, 11.6798556),
//                Lists.newArrayList(48.16345841, 11.58302009),
//                Lists.newArrayList(48.18383423, 11.49727249),
//                Lists.newArrayList(48.14241331, 11.70490436),
//                Lists.newArrayList(48.16079907, 11.52672724),
//                Lists.newArrayList(48.14048169, 11.46420814),
//                Lists.newArrayList(48.19077688, 11.49226576),
//                Lists.newArrayList(48.10875989, 11.69375863),
//                Lists.newArrayList(48.11294724, 11.64286724),
//                Lists.newArrayList(48.13507172, 11.65028968),
//                Lists.newArrayList(48.10772466, 11.63110409),
//                Lists.newArrayList(48.12128379, 11.65752931),
//                Lists.newArrayList(48.17341247, 11.61897519),
//                Lists.newArrayList(48.12023771, 11.56222694),
//                Lists.newArrayList(48.13731931, 11.62636257),
//                Lists.newArrayList(48.15523082, 11.47418263),
//                Lists.newArrayList(48.179338, 11.50153598),
//                Lists.newArrayList(48.11101983, 11.48014193),
//                Lists.newArrayList(48.19941996, 11.69347352),
//                Lists.newArrayList(48.13619544, 11.67101428),
//                Lists.newArrayList(48.1342571, 11.5796617),
//                Lists.newArrayList(48.15903506, 11.71716383),
//                Lists.newArrayList(48.13419007, 11.53645903),
//                Lists.newArrayList(48.14524821, 11.56718921),
//                Lists.newArrayList(48.14776636, 11.47525705),
//                Lists.newArrayList(48.19077723, 11.49894127),
//                Lists.newArrayList(48.16797845, 11.62916341),
//                Lists.newArrayList(48.13687878, 11.72030703),
//                Lists.newArrayList(48.12249197, 11.61985711),
//                Lists.newArrayList(48.12734948, 11.52255534),
//                Lists.newArrayList(48.12758731, 11.51504333),
//                Lists.newArrayList(48.13344902, 11.48846309),
//                Lists.newArrayList(48.19279735, 11.54450403),
//                Lists.newArrayList(48.15020144, 11.59077495),
//                Lists.newArrayList(48.14259499, 11.49875795),
//                Lists.newArrayList(48.13783028, 11.58181559),
//                Lists.newArrayList(48.18959036, 11.62912687),
//                Lists.newArrayList(48.12008846, 11.57491357),
//                Lists.newArrayList(48.18988096, 11.6510183),
//                Lists.newArrayList(48.10673863, 11.55905424),
//                Lists.newArrayList(48.14962822, 11.50431511),
//                Lists.newArrayList(48.18197897, 11.61766),
//                Lists.newArrayList(48.12854912, 11.64630685),
//                Lists.newArrayList(48.16045358, 11.50701185),
//                Lists.newArrayList(48.15242542, 11.61808868),
//                Lists.newArrayList(48.14207003, 11.5176365),
//                Lists.newArrayList(48.13330585, 11.47058638),
//                Lists.newArrayList(48.13775133, 11.61858772),
//                Lists.newArrayList(48.12519166, 11.46692414),
//                Lists.newArrayList(48.16298982, 11.67295172),
//                Lists.newArrayList(48.12122415, 11.48087098),
//                Lists.newArrayList(48.15722513, 11.53778107),
//                Lists.newArrayList(48.18872197, 11.51001058),
//                Lists.newArrayList(48.10855317, 11.55444414),
//                Lists.newArrayList(48.1982953, 11.6778831),
//                Lists.newArrayList(48.10650306, 11.6383268),
//                Lists.newArrayList(48.14006215, 11.50985203),
//                Lists.newArrayList(48.19387571, 11.6626471),
//                Lists.newArrayList(48.14856695, 11.64433752),
//                Lists.newArrayList(48.11961501, 11.63071741),
//                Lists.newArrayList(48.15636019, 11.69329075),
//                Lists.newArrayList(48.1819639, 11.46134074),
//                Lists.newArrayList(48.15867698, 11.62665703),
//                Lists.newArrayList(48.10803263, 11.64263206),
//                Lists.newArrayList(48.15643807, 11.71661075),
//                Lists.newArrayList(48.18343856, 11.6670836),
//                Lists.newArrayList(48.19049541, 11.50314574),
//                Lists.newArrayList(48.17597804, 11.48582331),
//                Lists.newArrayList(48.13602548, 11.55631573)
//        );
//    }


}

