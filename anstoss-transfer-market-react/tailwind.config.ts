import type {Config} from "tailwindcss";
import daisyUi from "daisyui";

export default {
    content: [
        "./pages/**/*.{js,ts,jsx,tsx,mdx}",
        "./components/**/*.{js,ts,jsx,tsx,mdx}",
        "./app/**/*.{js,ts,jsx,tsx,mdx}",
    ],
    plugins: [
        daisyUi,
    ],
} satisfies Config;
