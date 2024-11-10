import recognition.neuralnets.RecognitionPipelineLoadChooser;

public class Main {
    public static void main(String[] args) throws Exception {
        RecognitionPipelineLoadChooser nnModel = new RecognitionPipelineLoadChooser();
        String fileChoose = nnModel.fileChooser().toString();
        nnModel.loadModel(fileChoose);
        nnModel.showResults(fileChoose);

    }
}
