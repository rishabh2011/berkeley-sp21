package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {

    private final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private GuitarString[] strings;

    public GuitarHero() {
        strings = new GuitarString[keyboard.length()];
        for (int i = 0; i < strings.length; ++i) {
            GuitarString string = new GuitarString(440 * Math.pow(2, (i - 24) / 12));
            strings[i] = string;
        }
    }

    /**
     * Get the keyboard string
     */
    public String getKeyboard() {
        return keyboard;
    }

    /**
     * Get the GuitarString at the given index
     *
     * @param index the index to be fetched
     * @return GuitarString found at the given index | null for invalid index
     */
    public GuitarString getGuitarString(int index) {
        if (index >= 0 && index < strings.length) {
            return strings[index];
        }
        return null;
    }

    /**
     * Computes the superposition of all samples
     *
     * @return computed samples
     */
    public double computeSamples() {
        double samples = 0;
        for (int i = 0; i < strings.length; ++i) {
            samples += strings[i].sample();
        }
        return samples;
    }

    /**
     * Tic all guitar strings
     */
    public void tic() {
        for (int i = 0; i < strings.length; ++i) {
            strings[i].tic();
        }
    }

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarHero hero = new GuitarHero();
        GuitarString string = null;

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int keyIndex = hero.getKeyboard().indexOf(key);
                string = hero.getGuitarString(keyIndex);
                if (string != null) {
                    string.pluck();
                }
            }

            if (string != null) {
                /* compute the superposition of samples */
                double samples = hero.computeSamples();

                /* play the sample on standard audio */
                StdAudio.play(samples);

                /* advance the simulation of each guitar string by one step */
                hero.tic();
            }

        }
    }
}
