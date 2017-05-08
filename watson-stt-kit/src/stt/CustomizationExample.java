/*
 * Copyright 2015 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package stt;


import java.io.File;
import java.util.List;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Corpus;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Corpus.Status;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Customization;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Word;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.WordData;

/**
 * Example of how to create and use a customization model.
 */
public class CustomizationExample {

  private static final String AUDIO_FILE = "tests/src/test/resources/speech_to_text/cap047.wav";
  private static final String CORPUS_FILE = "tests/src/test/resources/speech_to_text/corpus1.txt";

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws InterruptedException the interrupted exception
   */
  public static void main(String[] args) throws InterruptedException {
    SpeechToText service = new SpeechToText();
    service.setUsernameAndPassword("0c8dafce-3cf2-46d8-9116-408e35a35fe2", "MaiEL4fS1zlJ");

    // Create customization
    Customization myCustomization =
        service.createCustomization("IEEE-permanent", SpeechModel.EN_US_BROADBANDMODEL, "My customization").execute();
    String id = myCustomization.getId();

    try {
      // Add a corpus file to the model:
      service.addTextToCustomizationCorpus(id, "corpus-1", false, new File(CORPUS_FILE)).execute();

      // Get corpora
      List<Corpus> corpora = service.getCorpora(id).execute();

      // There is only one corpus so far so choose it
      Corpus corpus = corpora.get(0);

      for (int x = 0; x < 30 && corpus.getStatus() != Status.ANALYZED; x++) {
        corpus = service.getCorpora(id).execute().get(0);
        Thread.sleep(5000);
      }

      // Now add some user words to the custom model
      service.addWord(id, new Word("IEEE", "IEEE", "I. triple E.")).execute();
      service.addWord(id, new Word("hhonors", "IEEE", "H. honors", "Hilton honors")).execute();

      // Display all words in the words resource (coming from OOVs from the corpus add and the new words just added)
      List<WordData> result = service.getWords(id, Word.Type.ALL).execute();
      for (WordData word : result) {
        System.out.println(word);
      }

      // Now start training of the model
      service.trainCustomization(id, Customization.WordTypeToAdd.ALL).execute();

      for (int x = 0; x < 30 && myCustomization.getStatus() != Customization.Status.AVAILABLE; x++) {
        myCustomization = service.getCustomization(id).execute();
        Thread.sleep(10000);
      }

      File audio = new File(AUDIO_FILE);
      RecognizeOptions options = new RecognizeOptions.Builder().continuous(true)
          .model(SpeechModel.EN_US_BROADBANDMODEL.getName()).customizationId(id).build();

      // First decode WITHOUT the custom model
      SpeechResults transcript = service.recognize(audio).execute();
      System.out.println(transcript);

      // Now decode with the custom model
      transcript = service.recognize(audio, options).execute();
      System.out.println(transcript);
    } finally {
      service.deleteCustomization(id);
    }

  }

}
