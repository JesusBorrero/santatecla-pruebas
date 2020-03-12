package com;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.card.Card;
import com.card.CardRepository;
import com.course.Course;
import com.course.CourseRepository;
import com.itinerary.lesson.Lesson;
import com.itinerary.lesson.LessonRepository;
import com.itinerary.module.Module;
import com.itinerary.module.ModuleRepository;
import com.question.Question;
import com.question.definition.definition_answer.DefinitionAnswer;
import com.question.definition.definition_question.DefinitionQuestion;
import com.question.definition.definition_question.DefinitionQuestionRepository;
import com.question.list.list_answer.ListAnswer;
import com.question.list.list_question.ListQuestion;
import com.question.list.list_question.ListQuestionRepository;
import com.question.test.test_answer.TestAnswer;
import com.question.test.test_question.TestQuestion;
import com.question.test.test_question.TestQuestionRepository;
import com.relation.Relation;
import com.relation.RelationRepository;
import com.slide.Slide;
import com.slide.SlideRepository;
import com.unit.Unit;
import com.unit.UnitRepository;
import com.user.User;
import com.user.UserRepository;

import com.image.Image;
import com.image.ImageRepository;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    
        @Autowired
        private CardRepository cardRepository;

        @Autowired
        private LessonRepository lessonRepository;
    
        @Autowired
        private UnitRepository unitRepository;

        @Autowired
        private ModuleRepository moduleRepository;
    
        @Autowired
        private RelationRepository relationRepository;

        @Autowired
        private SlideRepository slideRepository;
  
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private DefinitionQuestionRepository definitionQuestionRepository;

        @Autowired
        private ListQuestionRepository listQuestionRepository;

        @Autowired
        private TestQuestionRepository testQuestionRepository;
  
        @Autowired
        private CourseRepository courseRepository;
        

        @PostConstruct
        public void init() {

                //Slides
                Slide slide1 = new Slide("Programación Declarativa vs Programación Imperativa");
                Slide slide01 = new Slide("¿Qué es la Programación Funcional?");
                Slide slide02 = new Slide("Evolución de la Programación Funcional");
                Slide slide03 = new Slide("Paradigmas de la programación");
                Slide slide04 = new Slide("Ejemplo de añadir una slide");
                Slide slide2 = new Slide("Tipos de datos");
                Slide slide3 = new Slide("Funciones recursivas");
                Slide slide4 = new Slide("Funciones de orden superior");
                Slide slide5 = new Slide("Sinónimos de tipo");
                Slide slide6 = new Slide("Historia");
                Slide slide7 = new Slide("Tipos de datos compuestos");
                Slide slide8 = new Slide("Estructuras");
                Slide slide9 = new Slide("¿Qué es?");
                Slide slide10 = new Slide("Introducción");
                Slide slide11 = new Slide("Compiladores e interpretes");
                Slide slide12 = new Slide("Funciones");

                slide1.addContent("\nLos programas de computador constituyen una\n" +
                        "representación de uno o más algoritmos diseñados para\n" +
                        "resolver problemas reales\n" +
                        "\n" +
                        "En un programa de computador intervienen dos\n" +
                        "elementos principales:\n" +
                        "\n" +
                        "* La lógica: “qué objetivo se desea alcanzar”\n" +
                        "* El control: “cómo alcanzar dicho objetivo”\n" +
                        "\n" +
                        "La programación declarativa es un paradigma de\n" +
                        "programación que expresa la lógica de computación sin\n" +
                        "describir el flujo de control\n" +
                        "\n" +
                        "Constituye un estilo de programación contrapuesto al\n" +
                        "estilo imperativo, que se centra tanto el objetivo final\n" +
                        "como en la manera de alcanzarlo, estableciendo una\n" +
                        "secuencia pasos para ello\n" +
                        "\n" +
                        "El paradigma *imperativo* está basado en el concepto de\n" +
                        "nombres de variables que pueden ser asociadas a\n" +
                        "diferentes valores, empleando expresiones de\n" +
                        "asignación\n" +
                        "\n" +
                        "En el paradigma *declarativo* no existe el concepto de\n" +
                        "variable como tal que pueda ser alterada durante la\n" +
                        "ejecución de un programa, sino que un valor asignado a\n" +
                        "una “variable” permanece constante\n");

                slide01.addContent("\nEl paradigma de la programación funcional es un estilo\n" +
                        "de computación que sigue la evaluación de funciones\n" +
                        "matemáticas y evita los estados intermedios\n" +
                        "\n" +
                        "En un sentido estricto, la programación funcional define\n" +
                        "un programa como una función matemática que\n" +
                        "convierte unas entradas en unas salidas, sin ningún\n" +
                        "estado interno y ningún efecto lateral\n");

                slide02.addContent("\nDe los años 20 y 30 proviene la teoría de funciones\n" +
                        "como modelo de computación. Los fundadores entre\n" +
                        "otros son: Haskell Curry y Alonzo Church\n" +
                        "\n" +
                        "El primer lenguaje funcional desarrollado fue LISP\n" +
                        "(1958), de John McCarthy, que se aplicó en el área de\n" +
                        "Inteligencia Artificial\n" +
                        "\n" +
                        "* LISP es un lenguaje revolucionario que introduce nuevos\n" +
                        "conceptos de programación: funciones como objetos primitivos,\n" +
                        "funciones de orden superior, polimorfismo, etc.\n" +
                        "\n" +
                        "Años 80: Eclosión de lenguajes funcionales SASL, KRC\n" +
                        "y Miranda, Haskell, Hope, Wadler, CAML, etc.\n" +
                        "\n" +
                        "* Años 70, 80 y 90 se utilizaba en el ámbito académico y de\n" +
                        "investigación, pero en la Empresa se utilizaban los lenguajes\n" +
                        "imperativos y OO\n");

                slide03.addContent("\n" +
                        "insert.card/Array/Definición\n" +
                        "\n" +
                        "insert.card/1/2\n");

                slide04.addContent("\n" +
                        "insert.slide/Programación funcional/Introducción a la programación funcional/Paradigmas de la programación\n");

                slideRepository.save(slide1);
                slideRepository.save(slide01);
                slideRepository.save(slide02);
                slideRepository.save(slide03);
                slideRepository.save(slide04);
                slideRepository.save(slide2);
                slideRepository.save(slide3);
                slideRepository.save(slide4);
                slideRepository.save(slide5);
                slideRepository.save(slide6);
                slideRepository.save(slide7);
                slideRepository.save(slide8);
                slideRepository.save(slide9);
                slideRepository.save(slide10);
                slideRepository.save(slide11);
                slideRepository.save(slide12);

                //Lesson
                Lesson lesson1 = new Lesson("Introducción a la programación funcional");
                Lesson lesson2 = new Lesson("Introducción a Haskell");
                Lesson lesson3 = new Lesson("Introducción a la recursividad");
                Lesson lesson4 = new Lesson("Funciones de orden superior");
                Lesson lesson5 = new Lesson("Declaración, Clases y Tipos");
                Lesson lesson6 = new Lesson("Introducción a C");
                Lesson lesson7 = new Lesson("Arrays y funciones en C");
                Lesson lesson8 = new Lesson("Estructuras y punteros en C");
                Lesson lesson9 = new Lesson("Memoria dinámica");
                Lesson lesson10 = new Lesson("Ficheros");
                Lesson lesson11 = new Lesson("Introducción, Tipos de datos, Operadores y Estructura de control");
                Lesson lesson12 = new Lesson("Funciones y Objetos");

                lesson1.getSlides().add(slide1);
                lesson1.getSlides().add(slide01);
                lesson1.getSlides().add(slide02);
                lesson1.getSlides().add(slide03);
                lesson1.getSlides().add(slide04);
                lesson2.getSlides().add(slide2);
                lesson3.getSlides().add(slide3);
                lesson4.getSlides().add(slide4);
                lesson5.getSlides().add(slide5);
                lesson6.getSlides().add(slide6);
                lesson7.getSlides().add(slide7);
                lesson8.getSlides().add(slide8);
                lesson9.getSlides().add(slide9);
                lesson10.getSlides().add(slide10);
                lesson11.getSlides().add(slide11);
                lesson12.getSlides().add(slide12);

                lessonRepository.save(lesson1);
                lessonRepository.save(lesson2);
                lessonRepository.save(lesson3);
                lessonRepository.save(lesson4);
                lessonRepository.save(lesson5);
                lessonRepository.save(lesson6);
                lessonRepository.save(lesson7);
                lessonRepository.save(lesson8);
                lessonRepository.save(lesson9);
                lessonRepository.save(lesson10);
                lessonRepository.save(lesson11);
                lessonRepository.save(lesson12);

                //Modules
                Module module1 = new Module("Programación funcional");
                Module module2 = new Module("Programación imperativa estructurada");
                Module module3 = new Module("Programación con lenguajes interpretados");
                Module module4 = new Module("Paradigmas de la programación");
                Module module5 = new Module("Introducción a la programación funcional");
                Module module6 = new Module("Introducción a Haskell");
                Module module7 = new Module("Recursividad. Funciones de orden superior");
                Module module8 = new Module("Tipos de datos definidos por el programador. Sistema de Clases");
                Module module9 = new Module("Introducción a C");
                Module module10 = new Module("Arrays y funciones en C");
                Module module11 = new Module("Estructuras y punteros en C");
                Module module12 = new Module("Memoria dinámica");
                Module module13 = new Module("Ficheros");
                Module module14 = new Module("Introducción a Python");

                moduleRepository.save(module1);
                moduleRepository.save(module2);
                moduleRepository.save(module3);
                moduleRepository.save(module4);
                moduleRepository.save(module5);
                moduleRepository.save(module6);
                moduleRepository.save(module7);
                moduleRepository.save(module8);
                moduleRepository.save(module9);
                moduleRepository.save(module10);
                moduleRepository.save(module11);
                moduleRepository.save(module12);
                moduleRepository.save(module13);
                moduleRepository.save(module14);

                module5.addBlock(lesson1);
                module6.addBlock(lesson2);
                module7.addBlock(lesson3);
                module7.addBlock(lesson4);
                module8.addBlock(lesson5);
                module9.addBlock(lesson6);
                module10.addBlock(lesson7);
                module11.addBlock(lesson8);
                module12.addBlock(lesson9);
                module13.addBlock(lesson10);
                module14.addBlock(lesson11);
                module14.addBlock(lesson12);

                module1.addBlock(module5);
                module1.addBlock(module6);
                module1.addBlock(module7);
                module1.addBlock(module8);

                module2.addBlock(module9);
                module2.addBlock(module10);
                module2.addBlock(module11);
                module2.addBlock(module12);
                module2.addBlock(module13);

                module3.addBlock(module14);

                module4.addBlock(module1);
                module4.addBlock(module2);
                module4.addBlock(module3);

                moduleRepository.save(module1);
                moduleRepository.save(module2);
                moduleRepository.save(module3);
                moduleRepository.save(module4);
                moduleRepository.save(module5);
                moduleRepository.save(module6);
                moduleRepository.save(module7);
                moduleRepository.save(module8);
                moduleRepository.save(module9);
                moduleRepository.save(module10);
                moduleRepository.save(module11);
                moduleRepository.save(module12);
                moduleRepository.save(module13);
                moduleRepository.save(module14);
/*
                // Definition Questions
                DefinitionQuestion definition1 = new DefinitionQuestion("¿Qué es el software?");
                definition1.addModule(module1);
                DefinitionQuestion definition2 = new DefinitionQuestion("¿Qué es Java?");
                definition1.addModule(module2);

                definitionQuestionRepository.save(definition1);
                definitionQuestionRepository.save(definition2);

                // List Questions
                ArrayList<String> possibleAnswers = new ArrayList<>();
                possibleAnswers.add("Java");
                possibleAnswers.add("Javascript");
                possibleAnswers.add("Python");
                ArrayList<String> correctAnswer = new ArrayList<>();
                correctAnswer.add("Python");
                correctAnswer.add("Java");
                ListQuestion list1 = new ListQuestion("¿Cuáles de los siguientes son lenguajes de programación?",
                        possibleAnswers, correctAnswer);
                list1.addModule(module1);

                listQuestionRepository.save(list1);

                //Test Questions
                List<String> testAnswers = new ArrayList<>();
                testAnswers.add("Sí");
                testAnswers.add("No");
                TestQuestion test = new TestQuestion("¿Es Java un lenguaje de programación?", testAnswers, "Sí");
                test.addModule(module1);

                testQuestionRepository.save(test);
*/
                //Paradigmas de programación: Questions
                List<DefinitionQuestion> unit1Definitions = new ArrayList<>();
                List<ListQuestion> unit1Lists = new ArrayList<>();
                List<TestQuestion> unit1Tests = new ArrayList<>();

                DefinitionQuestion definition1 = new DefinitionQuestion("¿Qué es un paradigma de programación?");
                DefinitionQuestion definition2 = new DefinitionQuestion("Defina, según sus palabras, un tipo de paradigma");
                DefinitionQuestion definition3 = new DefinitionQuestion("Defina, según sus palabras, dos tipos de paradigmas");
                DefinitionQuestion definition4 = new DefinitionQuestion("Defina, según sus palabras, tres tipos de paradigmas");

                //Corrected
                List<DefinitionAnswer> defList = new ArrayList<>();
                DefinitionAnswer da1 = new DefinitionAnswer("Un problema al programar");
                da1.setCorrected(true);
                da1.setCorrect(false);
                DefinitionAnswer da2 = new DefinitionAnswer("Un lenguaje de programación");
                da2.setCorrected(true);
                da2.setCorrect(false);
                DefinitionAnswer da3 = new DefinitionAnswer("Un error dificil de resolver al programar");
                da3.setCorrected(true);
                da3.setCorrect(false);
                DefinitionAnswer da4 = new DefinitionAnswer("Programar usando patrones de diseño");
                da4.setCorrected(true);
                da4.setCorrect(false);
                DefinitionAnswer da5 = new DefinitionAnswer("Es parecido a un patrón");
                da5.setCorrected(true);
                da5.setCorrect(false);
                DefinitionAnswer da6 = new DefinitionAnswer("Es un modelo básico de diseño y desarrollo de programas");
                da6.setCorrected(true);
                da6.setCorrect(true);
                DefinitionAnswer da7 = new DefinitionAnswer("Es un modelo básico de diseño y desarrollo de programas siguiendo unas normas");
                da7.setCorrected(true);
                da7.setCorrect(true);
                defList.add(da1);
                defList.add(da2);
                defList.add(da3);
                defList.add(da4);
                defList.add(da5);
                defList.add(da6);
                defList.add(da7);

                //Not corrected
                DefinitionAnswer da8 = new DefinitionAnswer("Son los distintos lenguajes para programar");
                DefinitionAnswer da9 = new DefinitionAnswer("Es una manera de programar usando distintas estrategias");
                DefinitionAnswer da10 = new DefinitionAnswer("Programar siguiento unas normas para abordar distintos tipos de problemas");
                DefinitionAnswer da11 = new DefinitionAnswer("Son patrones de diseño");
                defList.add(da8);
                defList.add(da9);
                defList.add(da10);
                defList.add(da11);
                
                definition1.setAnswers(defList);
                definition1.setTotalCorrectAnswers(2);
                definition1.setTotalWrongAnswers(5);

                definition2.addBlock(lesson1);
                definition3.addBlock(lesson1);
                definition3.addBlock(lesson2);
                definition4.addBlock(lesson3);

                unit1Definitions.add(definition1);
                unit1Definitions.add(definition2);
                unit1Definitions.add(definition3);
                unit1Definitions.add(definition4);

                ArrayList<String> possibleAnswers = new ArrayList<>();
                possibleAnswers.add("Paradigma funcional");
                possibleAnswers.add("Paradigma lógico");
                possibleAnswers.add("Paradigma orientado a objetos");
                possibleAnswers.add("Paradigma imperativo");
                TestQuestion test1 = new TestQuestion("Los lenguajes comunes de este paradigma son: LISP, Scheme, Haskell, Scala", possibleAnswers, "Paradigma funcional");
                TestAnswer testAnswer1 = new TestAnswer();
                testAnswer1.setAnswerText("Paradigma lógico");
                testAnswer1.setCorrect(false);
                TestAnswer testAnswer2 = new TestAnswer();
                testAnswer2.setAnswerText("Paradigma lógico");
                testAnswer2.setCorrect(false);
                TestAnswer testAnswer3 = new TestAnswer();
                testAnswer3.setAnswerText("Paradigma lógico");
                testAnswer3.setCorrect(false);
                TestAnswer testAnswer4 = new TestAnswer();
                testAnswer4.setAnswerText("Paradigma orientado a objetos");
                testAnswer4.setCorrect(false);
                TestAnswer testAnswer5 = new TestAnswer();
                testAnswer5.setAnswerText("Paradigma imperativo");
                testAnswer5.setCorrect(false);
                TestAnswer testAnswer6 = new TestAnswer();
                testAnswer6.setAnswerText("Paradigma imperativo");
                testAnswer6.setCorrect(false);
                TestAnswer testAnswer7 = new TestAnswer();
                testAnswer7.setAnswerText("Paradigma funcional");
                testAnswer7.setCorrect(true);

                test1.addAnswer(testAnswer1);
                test1.addAnswer(testAnswer2);
                test1.addAnswer(testAnswer3);
                test1.addAnswer(testAnswer4);
                test1.addAnswer(testAnswer5);
                test1.addAnswer(testAnswer6);
                test1.addAnswer(testAnswer7);
                test1.setTotalCorrectAnswers(1);
                test1.setTotalWrongAnswers(6);

                possibleAnswers = new ArrayList<>();
                possibleAnswers.add("Programación orientada a objetos");
                possibleAnswers.add("Programación lógica");
                possibleAnswers.add("Programación estructurada");
                possibleAnswers.add("Programación lineal");
                TestQuestion test2 = new TestQuestion( "Forma de escribir programación, " +
                        "utilizando solo tres estructuras: secuencial, selectiva e iterativa",
                        possibleAnswers, "Programación estructurada");

                unit1Tests.add(test1);
                unit1Tests.add(test2);

                possibleAnswers = new ArrayList<>();
                possibleAnswers.add("Paradigma euclídeo");
                possibleAnswers.add("Paradigma orientado a estructuras");
                possibleAnswers.add("Paradigma orientado a objetos");
                possibleAnswers.add("Paradigmal imperativo");
                List<String> correctAnswers = new ArrayList<>();
                correctAnswers.add("Paradigma orientado a objetos");
                correctAnswers.add("Paradigmal imperativo");
                ListQuestion list1 = new ListQuestion("¿Cuáles son paradigmas?", possibleAnswers, correctAnswers);
                ListAnswer listAnswer1 = new ListAnswer();
                List<String> answers1 = new ArrayList<>();
                answers1.add("Paradigma euclídeo");
                answers1.add("Paradigma orientado a estructuras");
                listAnswer1.setAnswer(answers1);
                listAnswer1.setCorrect(false);
                ListAnswer listAnswer2 = new ListAnswer();
                List<String> answers2 = new ArrayList<>();
                answers2.add("Paradigma euclídeo");
                listAnswer2.setAnswer(answers2);
                listAnswer2.setCorrect(false);
                ListAnswer listAnswer3 = new ListAnswer();
                List<String> answers3 = new ArrayList<>();
                answers3.add("Paradigma euclídeo");
                answers3.add("Paradigma orientado a estructuras");
                answers3.add("Paradigmal imperativo");
                listAnswer3.setAnswer(answers3);
                listAnswer3.setCorrect(false);
                ListAnswer listAnswer4 = new ListAnswer();
                List<String> answers4 = new ArrayList<>();
                answers4.add("Paradigma orientado a objetos");
                answers4.add("Paradigmal imperativo");
                listAnswer4.setAnswer(answers4);
                listAnswer4.setCorrect(true);
                ListAnswer listAnswer5 = new ListAnswer();
                List<String> answers5 = new ArrayList<>();
                answers5.add("Paradigmal imperativo");
                answers5.add("Paradigma orientado a estructuras");
                listAnswer5.setAnswer(answers5);
                listAnswer5.setCorrect(false);
                ListAnswer listAnswer6 = new ListAnswer();
                List<String> answers6 = new ArrayList<>();
                answers6.add("Paradigma euclídeo");
                answers6.add("Paradigma orientado a estructuras");
                answers6.add("Paradigma orientado a objetos");
                answers6.add("Paradigmal imperativo");
                listAnswer6.setAnswer(answers6);
                listAnswer6.setCorrect(false);

                list1.addAnswer(listAnswer1);
                list1.addAnswer(listAnswer2);
                list1.addAnswer(listAnswer3);
                list1.addAnswer(listAnswer4);
                list1.addAnswer(listAnswer5);
                list1.addAnswer(listAnswer6);

                list1.setTotalCorrectAnswers(1);
                list1.setTotalWrongAnswers(5);

                unit1Lists.add(list1);

                definitionQuestionRepository.save(definition1);
                definitionQuestionRepository.save(definition2);
                definitionQuestionRepository.save(definition3);
                definitionQuestionRepository.save(definition4);
                listQuestionRepository.save(list1);
                testQuestionRepository.save(test1);
                testQuestionRepository.save(test2);

                lesson1.getQuestionsIds().add(definition2.getId());
                lesson1.getQuestionsIds().add(definition3.getId());
                lesson2.getQuestionsIds().add(definition3.getId());
                lesson3.getQuestionsIds().add(definition4.getId());

                lessonRepository.save(lesson1);
                lessonRepository.save(lesson2);
                lessonRepository.save(lesson3);
                lessonRepository.save(lesson4);
                lessonRepository.save(lesson5);
                lessonRepository.save(lesson6);
                lessonRepository.save(lesson7);
                lessonRepository.save(lesson8);
                lessonRepository.save(lesson9);
                lessonRepository.save(lesson10);
                lessonRepository.save(lesson11);
                lessonRepository.save(lesson12);

                //Units
                List<Unit> units = new ArrayList<>();
                Unit unit1 = new Unit("Paradigma de programación");
                units.add(unit1);
                Unit unit2 = new Unit("Programación funcional");
                units.add(unit2);
                Unit unit3 = new Unit("Programación imperativa");
                units.add(unit3);
                Unit unit4 = new Unit("Lenguaje de programación");
                units.add(unit4);
                Unit unit5 = new Unit("Haskell");
                units.add(unit5);
                Unit unit6 = new Unit("C");
                units.add(unit6);
                Unit unit7 = new Unit("Python");
                units.add(unit7);
                Unit unit8 = new Unit("Función");
                units.add(unit8);
                Unit unit9 = new Unit("Función recursiva");
                units.add(unit9);
                Unit unit10 = new Unit("Función recursiva lineal");
                units.add(unit10);
                Unit unit11 = new Unit("Función recursiva múltiple");
                units.add(unit11);
                Unit unit12 = new Unit("Función de orden superior");
                units.add(unit12);
                Unit unit13 = new Unit("Tipo de dato");
                units.add(unit13);
                Unit unit14 = new Unit("Clase");
                units.add(unit14);
                Unit unit15 = new Unit("Lenguaje interpretado");
                units.add(unit15);
                Unit unit16 = new Unit("Array");
                units.add(unit16);
                Unit unit17 = new Unit("Fichero");
                units.add(unit17);

                // Cards
                Card card1_1 = new Card("Definición",
                        "Un _paradigma de programación_ es un *estilo de desarrollo de programas*, es decir, " +
                                "un modelo para resolver problemas computacionales.");
                Card card1_2 = new Card("Diferencias entre los tipos de paradigmas",
                        "Los paradigmas difieren unos de otros en los *conceptos* y la *forma de abstraer* los elementos " +
                                "involucrados en un problema, así como en los *pasos* que integran su solución del problema, " +
                                "en otras palabras, el cómputo.");
                cardRepository.save(card1_1);
                cardRepository.save(card1_2);
                unit1.addCard(card1_1);
                unit1.addCard(card1_2);
                Card card2_1 = new Card("Definición",
                        "La _programación funcional_ es un paradigma de programación *basado en el uso " +
                                "de funciones matemáticas*. Tiene sus raíces en el cálculo lambda, un sistema formal desarrollado " +
                                "para investigar la definición de _función_, su aplicación y la recursión.");
                Card card2_2 = new Card("Ventajas",
                                " - Ausencia de efectos colaterales\n" +
                                " - Proceso de depuración menos problemático\n" +
                                " - Pruebas de unidades más confiables\n" +
                                " - Mayor facilidad para la ejecución concurrente");
                cardRepository.save(card2_1);
                cardRepository.save(card2_2);
                unit2.addCard(card2_1);
                unit2.addCard(card2_2);
                Card card3_1 = new Card("Definición",
                        "La _programación imperativa_ es un paradigma de programación en el que un programa se describe en términos " +
                                "de *instrucciones, condiciones y pasos* que modifican el estado de un programa al permitir la mutación de " +
                                "variables, todo esto con el objetivo de llegar a un resultado.");
                Card card3_2 = new Card("Ventajas",
                        " - Relativa simplicidad y facilidad de impletentación, así como el seguimiento del flujo del programa\n" +
                                " - Modularización");
                cardRepository.save(card3_1);
                cardRepository.save(card3_2);
                unit3.addCard(card3_1);
                unit3.addCard(card3_2);
                Card card4_1 = new Card("Definición",
                        "Un _lenguaje de programación_ es un *idioma artificial* diseñado para expresar computaciones " +
                                "que pueden ser llevadas a cabo por máquinas como las computadoras.\n" +
                                "Le proporciona al programador la capacidad de escribir (programar) una serie de instrucciones " +
                                "o secuencias de órdenes en forma de algoritmos con el fin de *controlar el comportamiento de una " +
                                "computadora, de manera que se puedan obtener diversas clases de datos*");
                cardRepository.save(card4_1);
                unit4.addCard(card4_1);
                Card card5_1 = new Card("Definición",
                        "_Haskell_ es un lenguaje de programación moderno, estándar, no estricto, puramente funcional.");
                Card card5_2 = new Card("Características",
                        "Posee todas las características avanzadas, incluyendo *polimorfismo de tipos*, " +
                                "*evaluación perezosa* y *funciones de alto orden*. También es un tipo de sistema " +
                                "que soporta una forma sistemática de sobrecarga y un sistema modular.");
                Card card5_3 = new Card("Por qué usar Haskell",
                        "Es particularmente apropiado para programas que necesitan ser altamente modificados y mantenidos, " +
                                "de forma fácil y barata.");
                cardRepository.save(card5_1);
                cardRepository.save(card5_2);
                cardRepository.save(card5_3);
                unit5.addCard(card5_1);
                unit5.addCard(card5_2);
                unit5.addCard(card5_3);
                Card card6_1 = new Card("Definición",
                        "C es un lenguaje de programación orientado a la implementación de sistemas operativos, por su *eficiencia* del " +
                                "código. Es el lenguaje de programación más popular para crear software de sistema, aunque también se " +
                                "utiliza para crear aplicaciones.");
                Card card6_2 = new Card("Características",
                        "Se trata de un lenguaje de tipos de *datos estáticos, débilmente tipificado, de medio nivel*, " +
                                "ya que dispone de las estructuras típicas de los lenguajes de alto nivel pero, a su vez, dispone " +
                                "de construcciones del lenguaje que permiten un control a muy bajo nivel.");
                cardRepository.save(card6_1);
                cardRepository.save(card6_2);
                unit6.addCard(card6_1);
                unit6.addCard(card6_2);
                Card card7_1 = new Card("Definición",
                        "_Python_ es un lenguaje de programación interpretado cuya filosofía hace hincapié en la *legibilidad de su código*.");
                Card card7_2 = new Card("Características",
                        "Es un lenguaje de programación *multiparadigma*, ya que soporta orientación a objetos, " +
                                "programación imperativa y, en menor medida, programación funcional. Es un lenguaje *interpretado, " +
                                "dinámico y multiplataforma*.");
                cardRepository.save(card7_1);
                cardRepository.save(card7_2);
                unit7.addCard(card7_1);
                unit7.addCard(card7_2);
                Card card8_1 = new Card("Definición",
                        "Una _función_ es un conjunto de líneas de código que *realizan una tarea específica* y puede retornar un valor. " +
                                "Pueden tomar parámetros que modifiquen su funcionamiento. Son utilizadas para descomponer grandes problemas en " +
                                "tareas simples y para implementar operaciones que son comúnmente utilizadas durante un programa.");
                cardRepository.save(card8_1);
                unit8.addCard(card8_1);
                Card card9_1 = new Card("Definición",
                        "Se denominan _funciones recursivas_ a aquellas que *se llaman a sí mismas* durante su propia ejecución.");
                Card card9_2 = new Card("Ejemplos",
                        " - Método de ordenación quick-sort" +
                                " - Juego de las Torres de Hanoi");
                cardRepository.save(card9_1);
                cardRepository.save(card9_2);
                unit9.addCard(card9_1);
                unit9.addCard(card9_2);
                Card card10_1 = new Card("Definición",
                        "En una _función recursiva lineal_ cada llamada recursiva genera, *como mucho, otra llamada recursiva*.");
                cardRepository.save(card10_1);
                unit10.addCard(card10_1);
                Card card11_1 = new Card("Definición",
                        "Llamada recursiva que puede generar *más de una llamada* recursiva*.");
                Card card11_2 = new Card("Ejemplos",
                        " - La serie de Fibonacci\n" +
                                " - Fractales de árbol");
                cardRepository.save(card11_1);
                cardRepository.save(card11_2);
                unit11.addCard(card11_1);
                unit11.addCard(card11_2);
                Card card12_1 = new Card("Definición",
                        "Las _funciones de orden superior_ son funciones que *reciben una o más funciones como entrada o " +
                                "devuelven una función como salida*.");
                Card card12_2 = new Card("Utilidad",
                        "Se puede utilizar tanto la combinación de funciones de orden superior y como con expresiones lambda, " +
                                "lo que nos permite evitar escribir bucles.");
                cardRepository.save(card12_1);
                cardRepository.save(card12_2);
                unit12.addCard(card12_1);
                unit12.addCard(card12_2);
                Card card13_1 = new Card("Definición",
                        "Un _tipo de dato_ es la propiedad de un valor que determina su *dominio* (qué valores puede tomar), " +
                                "qué operaciones se le pueden aplicar y cómo es representado internamente por el computador.");
                cardRepository.save(card13_1);
                unit13.addCard(card13_1);
                Card card14_1 = new Card("Definición",
                        "Una _clase_ es una *plantilla* para la creación de objetos de datos según un modelo predefinido. " +
                                "Las clases se utilizan para representar *entidades o conceptos, como los sustantivos en el lenguaje*. " +
                                "Cada clase es un modelo que define un conjunto de variables, y cada objeto creado a partir de la " +
                                "clase se denomina instancia de la clase.");
                Card card14_2 = new Card("Utilización",
                        "Las clases de objetos son un pilar fundamental de la programación orientada a objetos. Permiten abstraer " +
                                "los datos y sus operaciones asociadas al modo de una caja negra.");
                Card card14_3 = new Card("Componentes",
                        " - *Campos de datos*: almacenan el estado de la clase por medio de variables, estructuras de datos e incluso otras clases.\n" +
                                " - *Métodos*: subrutinas de manipulación de dichos datos.\n" +
                                " - Ciertos lenguajes permiten un tercer tipo de miembro: las *propiedades*, a medio camino entre los campos y los métodos.");
                cardRepository.save(card14_1);
                cardRepository.save(card14_2);
                cardRepository.save(card14_3);
                unit14.addCard(card14_1);
                unit14.addCard(card14_2);
                unit14.addCard(card14_3);
                Card card15_1 = new Card("Definición",
                        "Un _lenguaje interpretado_ es el lenguaje cuyo código no necesita ser preprocesado mediante un compilador, " +
                                "eso significa que el ordenador es capaz de ejecutar la sucesión de instrucciones dadas por el programador " +
                                "sin necesidad de leer y traducir exhaustivamente todo el código.");
                Card card15_2 = new Card("Ventajas",
                        " - *Independiente de la máquina y del sistema operativo*. No contiene instrucciones propias de un procesador " +
                                "sino que contiene llamadas a funciones que el interprete deberá reconocer. Basta que exista un interprete de " +
                                "un lenguaje para dicho sistema y todos los programas escrito en ese lenguaje funcionarán.");
                Card card15_3 = new Card("Desventajas",
                        " - Velocidad\n" +
                                " - Portabilidad");
                cardRepository.save(card15_1);
                cardRepository.save(card15_2);
                cardRepository.save(card15_3);
                unit15.addCard(card15_1);
                unit15.addCard(card15_2);
                unit15.addCard(card15_3);
                Card card16_1 = new Card("Definición",
                        "Se llama _array o vector_ a una zona de almacenamiento *contiguo* que contiene una serie de elementos del mismo tipo.");
                Card card16_2 = new Card("Cuándo utilizar un array",
                        "Estas estructuras de datos son adecuadas para situaciones en las que el *volumen de los datos es de tamaño fijo* " +
                                "y el *acceso se realice de forma aleatoria*, si sabemos la posición de cada elemento.");
                cardRepository.save(card16_1);
                cardRepository.save(card16_2);
                unit16.addCard(card16_1);
                unit16.addCard(card16_2);
                Card card17_1 = new Card("Definición",
                        "Un _fichero o archivo_ informático es un *conjunto de bytes* que son almacenados en un dispositivo. " +
                                "Es identificado por un nombre y la descripción de la carpeta o directorio que lo contiene.");
                cardRepository.save(card17_1);
                unit17.addCard(card17_1);

/*
                ArrayList<DefinitionQuestion> unit1Questions = new ArrayList<>();
                unit1Questions.add((DefinitionQuestion)definition1);
                //unit1Questions.add((DefinitionQuestion)definition3);
                ArrayList<ListQuestion> unit1ListQuestions = new ArrayList<>();
                unit1ListQuestions.add((ListQuestion)list1);
                ArrayList<TestQuestion> unitTestQuestions = new ArrayList<>();
                unitTestQuestions.add(test);

                unit1.setDefinitionQuestions(unit1Questions);
                unit1.setListQuestions(unit1ListQuestions);
                unit1.setTestQuestions(unitTestQuestions);
                unit2.setTestQuestions(unitTestQuestions);


                unit1.addLesson(lesson1);
                unit2.addLesson(lesson2);
                unit4.addLesson(lesson3);

                unit1.getModules().add(module1);
                unit1.getModules().add(module2);
                unit1.getModules().add(module3);
                unit1.getModules().add(module4);
                unit1.getModules().add(module5);
*/

                unit2.addLesson(lesson1);
                unit5.addLesson(lesson2);
                unit9.addLesson(lesson3);
                unit12.addLesson(lesson4);
                unit4.addLesson(lesson5);
                unit6.addLesson(lesson6);
                unit6.addLesson(lesson7);
                unit6.addLesson(lesson8);
                unit6.addLesson(lesson9);
                unit17.addLesson(lesson10);
                unit7.addLesson(lesson11);
                unit7.addLesson(lesson12);

                unit1.getModules().add(module4);
                unit2.getModules().add(module1);
                unit3.getModules().add(module2);
                unit15.getModules().add(module3);
                unit2.getModules().add(module5);
                unit5.getModules().add(module6);
                unit5.getModules().add(module7);
                unit5.getModules().add(module8);
                unit6.getModules().add(module9);
                unit6.getModules().add(module10);
                unit6.getModules().add(module11);
                unit6.getModules().add(module12);
                unit6.getModules().add(module13);
                unit7.getModules().add(module14);

                unit1.setDefinitionQuestions(unit1Definitions);
                unit1.setListQuestions(unit1Lists);
                unit1.setTestQuestions(unit1Tests);

                for (Unit unit : units) {
                        unitRepository.save(unit);
                }

                //Relations
                Relation relation1 = new Relation(Relation.RelationType.INHERITANCE, unit1.getId(), unit2.getId());
                relationRepository.save(relation1);
                unit1.addIncomingRelation(relation1);
                unit2.addOutgoingRelation(relation1);
                Relation relation2 = new Relation(Relation.RelationType.INHERITANCE, unit1.getId(), unit3.getId());
                relationRepository.save(relation2);
                unit1.addIncomingRelation(relation2);
                unit3.addOutgoingRelation(relation2);
                Relation relation3 = new Relation(Relation.RelationType.ASSOCIATION, unit3.getId(), unit6.getId());
                relationRepository.save(relation3);
                unit3.addIncomingRelation(relation3);
                unit6.addOutgoingRelation(relation3);
                Relation relation4 = new Relation(Relation.RelationType.AGGREGATION, unit6.getId(), unit16.getId());
                relationRepository.save(relation4);
                unit6.addIncomingRelation(relation4);
                unit16.addOutgoingRelation(relation4);
                Relation relation5 = new Relation(Relation.RelationType.AGGREGATION, unit6.getId(), unit17.getId());
                relationRepository.save(relation5);
                unit6.addIncomingRelation(relation5);
                unit17.addOutgoingRelation(relation5);
                Relation relation6 = new Relation(Relation.RelationType.AGGREGATION, unit6.getId(), unit8.getId());
                relationRepository.save(relation6);
                unit6.addIncomingRelation(relation6);
                unit8.addOutgoingRelation(relation6);
                Relation relation7 = new Relation(Relation.RelationType.INHERITANCE, unit4.getId(), unit5.getId());
                relationRepository.save(relation7);
                unit4.addIncomingRelation(relation7);
                unit5.addOutgoingRelation(relation7);
                Relation relation9 = new Relation(Relation.RelationType.ASSOCIATION, unit2.getId(), unit5.getId());
                relationRepository.save(relation9);
                unit2.addIncomingRelation(relation9);
                unit5.addOutgoingRelation(relation9);
                Relation relation10 = new Relation(Relation.RelationType.AGGREGATION, unit5.getId(), unit8.getId());
                relationRepository.save(relation10);
                unit5.addIncomingRelation(relation10);
                unit8.addOutgoingRelation(relation10);
                Relation relation11 = new Relation(Relation.RelationType.AGGREGATION, unit5.getId(), unit8.getId());
                relationRepository.save(relation11);
                unit5.addIncomingRelation(relation11);
                unit8.addOutgoingRelation(relation11);
                Relation relation12 = new Relation(Relation.RelationType.INHERITANCE, unit8.getId(), unit9.getId());
                relationRepository.save(relation12);
                unit8.addIncomingRelation(relation12);
                unit9.addOutgoingRelation(relation12);
                Relation relation13 = new Relation(Relation.RelationType.INHERITANCE, unit8.getId(), unit12.getId());
                relationRepository.save(relation13);
                unit8.addIncomingRelation(relation13);
                unit12.addOutgoingRelation(relation13);
                Relation relation14 = new Relation(Relation.RelationType.INHERITANCE, unit9.getId(), unit10.getId());
                relationRepository.save(relation14);
                unit9.addIncomingRelation(relation14);
                unit10.addOutgoingRelation(relation14);
                Relation relation15 = new Relation(Relation.RelationType.INHERITANCE, unit9.getId(), unit11.getId());
                relationRepository.save(relation15);
                unit9.addIncomingRelation(relation15);
                unit11.addOutgoingRelation(relation15);
                Relation relation16 = new Relation(Relation.RelationType.AGGREGATION, unit5.getId(), unit14.getId());
                relationRepository.save(relation16);
                unit5.addIncomingRelation(relation16);
                unit14.addOutgoingRelation(relation16);
                Relation relation17 = new Relation(Relation.RelationType.AGGREGATION, unit5.getId(), unit13.getId());
                relationRepository.save(relation17);
                unit5.addIncomingRelation(relation17);
                unit13.addOutgoingRelation(relation17);
                Relation relation18 = new Relation(Relation.RelationType.INHERITANCE, unit4.getId(), unit15.getId());
                relationRepository.save(relation18);
                unit4.addIncomingRelation(relation18);
                unit15.addOutgoingRelation(relation18);
                Relation relation19 = new Relation(Relation.RelationType.INHERITANCE, unit15.getId(), unit7.getId());
                relationRepository.save(relation19);
                unit15.addIncomingRelation(relation19);
                unit7.addOutgoingRelation(relation19);
                Relation relation20 = new Relation(Relation.RelationType.AGGREGATION, unit7.getId(), unit8.getId());
                relationRepository.save(relation20);
                unit7.addIncomingRelation(relation20);
                unit8.addOutgoingRelation(relation20);
                Relation relation21 = new Relation(Relation.RelationType.AGGREGATION, unit7.getId(), unit13.getId());
                relationRepository.save(relation21);
                unit7.addIncomingRelation(relation21);
                unit13.addOutgoingRelation(relation21);
                Relation relation22 = new Relation(Relation.RelationType.AGGREGATION, unit7.getId(), unit17.getId());
                relationRepository.save(relation22);
                unit7.addIncomingRelation(relation22);
                unit17.addOutgoingRelation(relation22);

                for (Unit unit : units) {
                        unitRepository.save(unit);
                }

                //Users
                User user1 = new User("alumno", "alumno", "ROLE_USER");
                User user2 = new User("David Garcia", "alumno", "ROLE_USER");
                User user3 = new User("Jesús Ramírez", "alumno", "ROLE_USER");
                User user4 = new User("Alex Sánchez", "alumno", "ROLE_USER");
                User user5 = new User("Nacho Jiménez", "alumno", "ROLE_USER");
                User user6 = new User("Javi Gómez", "alumno", "ROLE_USER");
                User user7 = new User("Pedro Laborde", "alumno", "ROLE_USER");
                User user8 = new User("Maria Sánchez", "alumno", "ROLE_USER");
                User user9 = new User("Gema Caballero", "alumno", "ROLE_USER");
                User user10 = new User("Lucía Martín", "alumno", "ROLE_USER");
                User user11 = new User("Paula Goya", "alumno", "ROLE_USER");
                User user12 = new User("Lola Fernández", "alumno", "ROLE_USER");

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(user4);
                userRepository.save(user5);
                userRepository.save(user6);
                userRepository.save(user7);
                userRepository.save(user8);
                userRepository.save(user9);
                userRepository.save(user10);
                userRepository.save(user11);
                userRepository.save(user12);

                User teacher = new User("profesor", "profesor", "ROLE_ADMIN");
                userRepository.save(teacher);

                // Courses
                Course course = new Course("Lenguajes de programación", teacher, "Aprende lo básico de los lenguajes de programación más usados.");
                course.addStudent(user1);
                course.addStudent(user2);
                course.addStudent(user3);
                course.addStudent(user4);
                course.addStudent(user5);
                course.addStudent(user6);
                course.addStudent(user7);
                course.addStudent(user8);
                course.addStudent(user9);
                course.addStudent(user10);
                course.addStudent(user11);
                course.addStudent(user12);

                course.setModule(module4);
                courseRepository.save(course);

                Course course2 = new Course("Curso de Java", teacher, "Aprende todo lo necesario para ser un experto en Java.");
                course2.addStudent(user1);
                course2.addStudent(user2);
                course2.addStudent(user3);
                course2.addStudent(user4);
                course2.addStudent(user5);
                course2.addStudent(user6);
                course2.addStudent(user7);
                course2.addStudent(user8);
                course2.addStudent(user9);

                course2.setModule(module3);

            Course course3 = new Course("Curso de Python", teacher, "Aprende todo lo necesario para ser un experto en Python");
            course3.addStudent(user1);
            course3.addStudent(user2);
            course3.addStudent(user3);
            course3.addStudent(user4);
            course3.addStudent(user5);
            course3.addStudent(user6);
            course3.addStudent(user7);
            course3.addStudent(user8);
            course3.addStudent(user9);
            course3.addStudent(user10);
            course3.addStudent(user11);
            course3.addStudent(user12);

            course3.setModule(module4);
            courseRepository.save(course3);

            Course course4 = new Course("Curso de Testing", teacher, "Aprende todo lo necesario para ser un experto en Testing.");
            course4.addStudent(user1);
            course4.addStudent(user2);
            course4.addStudent(user3);
            course4.addStudent(user4);
            course4.addStudent(user5);
            course4.addStudent(user6);
            course4.addStudent(user7);
            course4.addStudent(user8);
            course4.addStudent(user9);

            course4.setModule(module3);

            courseRepository.save(course4);

            Course course5 = new Course("Investigación Operativa", teacher, "Aprende todo lo necesario para ser un experto en Pls");
            course5.addStudent(user1);
            course5.addStudent(user2);
            course5.addStudent(user3);
            course5.addStudent(user4);
            course5.addStudent(user5);
            course5.addStudent(user6);
            course5.addStudent(user7);
            course5.addStudent(user8);
            course5.addStudent(user9);
            course5.addStudent(user10);
            course5.addStudent(user11);
            course5.addStudent(user12);

            course5.setModule(module4);
            courseRepository.save(course5);

            Course course6 = new Course("Curso de Go", teacher, "Aprende todo lo necesario para ser un experto en Go.");
            course6.addStudent(user1);
            course6.addStudent(user2);
            course6.addStudent(user3);
            course6.addStudent(user4);
            course6.addStudent(user5);
            course6.addStudent(user6);
            course6.addStudent(user7);
            course6.addStudent(user8);
            course6.addStudent(user9);

            course6.setModule(module3);

            courseRepository.save(course6);

            DefinitionAnswer da12 = new DefinitionAnswer("Paradigma lógico");
            da12.setCorrect(true);
            da12.setBlockId(lesson1.getId());
            da12.setCourseId(1);
            da12.setUser(user1);
            definition2.addAnswer(da12);

            DefinitionAnswer da13 = new DefinitionAnswer("Paradigma lógico");
            da13.setCorrect(true);
            da13.setBlockId(lesson1.getId());
            da13.setCourseId(1);
            da13.setUser(user2);
            definition2.addAnswer(da13);

            DefinitionAnswer da14 = new DefinitionAnswer("Paradigma lógico");
            da14.setCorrect(true);
            da14.setBlockId(lesson1.getId());
            da14.setCourseId(1);
            da14.setUser(user3);
            definition2.addAnswer(da14);

            DefinitionAnswer da15 = new DefinitionAnswer("Paradigma lógico");
            da15.setCorrect(true);
            da15.setBlockId(lesson1.getId());
            da15.setCourseId(1);
            da15.setUser(user4);
            definition2.addAnswer(da15);

            DefinitionAnswer da16 = new DefinitionAnswer("Paradigma lógico");
            da16.setCorrect(true);
            da16.setBlockId(lesson1.getId());
            da16.setCourseId(1);
            da16.setUser(user5);
            definition2.addAnswer(da16);

            DefinitionAnswer da17 = new DefinitionAnswer("Paradigma lógico");
            da17.setCorrect(false);
            da17.setBlockId(lesson1.getId());
            da17.setCourseId(1);
            da17.setUser(user6);
            definition2.addAnswer(da17);

            definitionQuestionRepository.save(definition2);
	}

}
