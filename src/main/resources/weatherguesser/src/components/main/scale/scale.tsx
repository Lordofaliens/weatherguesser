import React, { useEffect, useRef } from "react";

interface ScaleProps {
    accuracy: number;
}

const Scale: React.FC<ScaleProps> = ({ accuracy }) => {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);
    let startAngle = Math.PI;
    let endAngle = Math.PI;
    let animationFrameId: number = 0; // Initialize animationFrameId with a default value

    const animate = () => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext("2d");
        if (!ctx) return;

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        endAngle += 0.03; // Adjust the increment to control the animation speed

        const gradient = ctx.createLinearGradient(
            Math.cos(startAngle) * 210 + 210,
            Math.sin(startAngle) * 105 + 105,
            420,
            210
        );

        gradient.addColorStop(0, 'rgba(245, 83, 61, 1)');
        gradient.addColorStop(1, 'rgba(61, 245, 150, 1)');

        ctx.strokeStyle = gradient;
        ctx.lineWidth = 20;
        ctx.lineCap = 'round';
        ctx.shadowColor = 'rgba(0, 0, 0, 0.4)';
        ctx.shadowBlur = 10;
        ctx.shadowOffsetX = 5;
        ctx.shadowOffsetY = 5;
        ctx.beginPath();
        ctx.arc(220, 210, 200, startAngle, endAngle);
        ctx.stroke();

        if (endAngle < accuracy) {
            animationFrameId = requestAnimationFrame(animate);
        }
    };

    useEffect(() => {
        const canvas = canvasRef.current;
        if (canvas) animate();

        return () => {
            cancelAnimationFrame(animationFrameId);
        };
    }, [accuracy]); // Include 'main' in the dependency array

    return <canvas ref={canvasRef} width={430} height={235} style={{zIndex:"1"}}/>;
}

export default Scale;
